import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { createRfp, extractRfp } from "../services/api"; // make sure this exists
import "../styles/createRfp.css";

function ItemRow({ item, index, onChange, onRemove }) {
  return (
    <div className="item-row" role="row">
      <input
        className="cell item-name"
        value={item.itemName}
        onChange={(e) => onChange(index, { ...item, itemName: e.target.value })}
        placeholder="Item name"
      />
      <input
        className="cell item-qty"
        type="number"
        min="0"
        value={item.quantity ?? ""}
        onChange={(e) => onChange(index, { ...item, quantity: Number(e.target.value) })}
        placeholder="Qty"
      />
      <input
        className="cell item-unit"
        value={item.unit ?? ""}
        onChange={(e) => onChange(index, { ...item, unit: e.target.value })}
        placeholder="Unit (e.g. pcs)"
      />
      <textarea
        className="cell item-specs"
        value={item.specsJson ?? ""}
        onChange={(e) => onChange(index, { ...item, specsJson: e.target.value })}
        placeholder='Specs JSON e.g. {"RAM":"16GB"}'
      />
      <button className="cell btn-remove" onClick={() => onRemove(index)} aria-label={`Remove item ${index}`}>
        Remove
      </button>
    </div>
  );
}

export default function CreateRfp() {
  const navigate = useNavigate();

  
  const [rfpText, setrfpText] = useState("");


  const [previewJson, setPreviewJson] = useState(null);
  const [previewRaw, setPreviewRaw] = useState("");

  
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [budget, setBudget] = useState("");
  const [deliveryDays, setDeliveryDays] = useState("");
  const [warranty, setWarranty] = useState("");
  const [items, setItems] = useState([]);

  const [loadingExtract, setLoadingExtract] = useState(false);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  function resetStructured() {
    setPreviewJson(null);
    setPreviewRaw("");
    setTitle("");
    setDescription("");
    setBudget("");
    setDeliveryDays("");
    setWarranty("");
    setItems([]);
  }

  async function handleExtract() {
    setError("");
    if (!rfpText || rfpText.trim().length < 10) {
      setError("Please paste the RFP text before extracting.");
      return;
    }

    setLoadingExtract(true);
    setPreviewJson(null);
    setPreviewRaw("");
    try {
      // backend expected to call LLM and return structured JSON (canonical schema).
      // Using createRfp endpoint for extraction is fine if your server supports it.
      // Here we call an extract endpoint; if your backend uses create directly, you can adapt.
      const payload = { rfpText }; // backend: call LLM and return structured JSON
      const res = await extractRfp({ rfpText, extractOnly: true }); // endpoint should honour extractOnly flag
      
      // server should return { structured: { ... } } or raw JSON string — adapt as per your backend
      const data = res.data;

      // Accept either raw string or object
      let structured = null;
      if (typeof data === "string") {
        // assume raw JSON text
        structured = JSON.parse(data);
        setPreviewRaw(data);
      } else if (data?.structured) {
        structured = data.structured;
        setPreviewRaw(JSON.stringify(structured, null, 2));
      } else if (data?.id) {
        // If create returned created entity, and includes structured in body
        structured = data;
        setPreviewRaw(JSON.stringify(structured, null, 2));
      } else {
        structured = data;
        setPreviewRaw(JSON.stringify(data, null, 2));
      }

      setPreviewJson(structured);

      // Map structured fields to editable form (best-effort)
      setTitle(structured.title ?? "");
      setDescription(structured.description ?? "");
      setBudget(structured.budget ?? "");
      setDeliveryDays(structured.delivery_timeline_days ?? "");
      setWarranty(structured.warranty ?? "");

      // items: normalize into our item shape
      const itemsFromLLM = Array.isArray(structured.items) ? structured.items : [];
      const normalized = itemsFromLLM.map((it) => ({
        itemName: it.item_name ?? it.name ?? "",
        quantity: Number(it.quantity ?? 0),
        unit: it.unit ?? "",
        specsJson: it.specs ? JSON.stringify(it.specs) : (it.specsJson ?? ""),
      }));
      setItems(normalized);

    } catch (e) {
      console.error(e);
      setError("Failed to extract. " + (e?.response?.data?.message || e.message));
    } finally {
      setLoadingExtract(false);
    }
  }

  function onItemChange(idx, next) {
    setItems((prev) => prev.map((p, i) => (i === idx ? next : p)));
  }
  function onItemRemove(idx) {
    setItems((prev) => prev.filter((_, i) => i !== idx));
  }
  function onAddItem() {
    setItems((prev) => [...prev, { itemName: "", quantity: 1, unit: "", specsJson: "" }]);
  }

  function validateBeforeSave() {
    if (!title || title.trim().length < 2) return "Title is required";
    if (!items || items.length === 0) return "At least one item is required";
    for (let i = 0; i < items.length; i++) {
      const it = items[i];
      if (!it.itemName || it.itemName.trim().length === 0) return `Item ${i + 1}: name required`;
      if (isNaN(Number(it.quantity)) || Number(it.quantity) <= 0) return `Item ${i + 1}: quantity must be > 0`;
      if (it.specsJson && it.specsJson.trim()) {
        try {
          JSON.parse(it.specsJson);
        } catch (ex) {
          return `Item ${i + 1}: specs must be valid JSON`;
        }
      }
    }
    return null;
  }

  async function handleSave() {
    setError("");
    const v = validateBeforeSave();
    if (v) {
      setError(v);
      return;
    }

    const payload = {
      title,
      description,
      budget: budget ? Number(budget) : null,
      deliveryTimelineDays: deliveryDays ? Number(deliveryDays) : null,
      warranty,
      rfpText,
      items: items.map((it) => ({
        itemName: it.itemName,
        quantity: Number(it.quantity),
        specs: it.specsJson ? JSON.parse(it.specsJson) : {},
        unit: it.unit,
      })),
    };

    setSaving(true);
    try {
      const res = await createRfp(payload); 
      
      const created = res.data;
      // assume created.id exists
      navigate(`/rfp/${created.id}`);
    } catch (e) {
      console.error(e);
      setError("Failed to save RFP. " + (e?.response?.data?.message || e.message));
    } finally {
      setSaving(false);
    }
  }

  return (
    <div className="create-rfp-root">
      <div className="left-col">
        <h2>Create RFP</h2>

        <label className="label">Paste raw RFP text</label>
        <textarea
          className="raw-text"
          value={rfpText}
          onChange={(e) => setrfpText(e.target.value)}
          placeholder="Paste the RFP description, requirements or vendor email content here..."
        />

        <div className="row actions">
          <button className="btn btn-primary" onClick={handleExtract} disabled={loadingExtract}>
            {loadingExtract ? "Extracting..." : "Extract Structured Data (LLM)"}
          </button>
          <button className="btn btn-secondary" onClick={resetStructured}>
            Reset
          </button>
        </div>

        {error && <div className="error">{error}</div>}

        <div className="form-block">
          <label className="label">Title</label>
          <input className="input" value={title} onChange={(e) => setTitle(e.target.value)} />

          <label className="label">Description</label>
          <textarea className="input" value={description} onChange={(e) => setDescription(e.target.value)} rows={4} />

          <div className="two-cols">
            <div>
              <label className="label">Budget</label>
              <input className="input" value={budget} onChange={(e) => setBudget(e.target.value)} placeholder="numeric" />
            </div>
            <div>
              <label className="label">Delivery Days</label>
              <input className="input" value={deliveryDays} onChange={(e) => setDeliveryDays(e.target.value)} placeholder="e.g. 30" />
            </div>
          </div>

          <label className="label">Warranty</label>
          <input className="input" value={warranty} onChange={(e) => setWarranty(e.target.value)} />

          <h3 className="section-title">Items</h3>
          <div className="items-header">
            <div className="head-cell">Item name</div>
            <div className="head-cell">Quantity</div>
            <div className="head-cell">Unit</div>
            <div className="head-cell">Specs (JSON)</div>
            <div className="head-cell">Action</div>
          </div>

          <div className="items-list" role="table">
            {items.map((it, idx) => (
              <ItemRow key={idx} item={it} index={idx} onChange={onItemChange} onRemove={onItemRemove} />
            ))}

            {items.length === 0 && <div className="muted">No items yet — click Add item to add one.</div>}
          </div>

          <div className="row" style={{ marginTop: 12 }}>
            <button className="btn" onClick={onAddItem}>Add Item</button>
            <div style={{ flex: 1 }} />
            <button className="btn btn-success" onClick={handleSave} disabled={saving}>
              {saving ? "Saving..." : "Save RFP"}
            </button>
          </div>
        </div>
      </div>

      <div className="right-col">
        <h3>Structured Preview</h3>
        <div className="preview-box">
          <pre className="preview-text">{previewRaw || "No structured preview yet. Click Extract."}</pre>
        </div>

        <div className="preview-actions">
          <button className="btn" onClick={() => { navigator.clipboard?.writeText(previewRaw || ""); }}>
            Copy JSON
          </button>
         
        </div>
      </div>
    </div>
  );
}
