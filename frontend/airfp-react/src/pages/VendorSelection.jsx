import React, { useEffect, useState } from "react";
import { getAllRfps, getAllVendors, sendRfpToVendors } from "../services/api";
import "../styles/VendorSelection.css";

export default function VendorSelection() {
  const [rfps, setRfps] = useState([]);
  const [vendors, setVendors] = useState([]);

  const [selectedRfp, setSelectedRfp] = useState(null);
  const [selectedVendors, setSelectedVendors] = useState([]);

  const [loading, setLoading] = useState(false);
  const [statusMessage, setStatusMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  // load RFPs + Vendors
  useEffect(() => {
    getAllRfps()
      .then((res) => setRfps(res.data))
      .catch(() => setErrorMessage("Failed to load RFPs"));

    getAllVendors()
      .then((res) => setVendors(res.data))
      .catch(() => setErrorMessage("Failed to load vendors"));
  }, []);

  const handleSendRfp = async () => {
    if (!selectedRfp || selectedVendors.length === 0) {
      setErrorMessage("Please select an RFP and at least one vendor.");
      return;
    }

    setErrorMessage("");
    setStatusMessage("");
    setLoading(true);

    try {
      const payload = {
        
        vendorIds: selectedVendors,
      };

      const res = await sendRfpToVendors(selectedRfp,payload);

      setStatusMessage("RFP sent successfully to vendors!");
    } catch (err) {
      setErrorMessage("Failed to send RFP. Try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="vs-container">

      {/* LOADING OVERLAY */}
      {loading && (
        <div className="vs-loading-overlay">
          <div className="vs-loading-box">
            <div className="vs-loader"></div>
            <p>Sending RFP to vendors...</p>
          </div>
        </div>
      )}

      <div className="vs-left">
        <h3>Select RFP</h3>
        <ul className="vs-rfp-list">
          {rfps.map((r) => (
            <li
              key={r.id}
              className={selectedRfp === r.id ? "active" : ""}
              onClick={() => setSelectedRfp(r.id)}
            >
              <div className="vs-rfp-title">{r.title}</div>
              <div className="vs-rfp-small">{r.description}</div>
            </li>
          ))}
        </ul>
      </div>

      <div className="vs-right">
        <h3>Select Vendors</h3>

        {errorMessage && <div className="vs-error">{errorMessage}</div>}
        {statusMessage && <div className="vs-success">{statusMessage}</div>}

        <div className="vs-vendor-list">
          {vendors.map((v) => (
            <div className="vs-vendor-card" key={v.id}>
              <label>
                <input
                  type="checkbox"
                  checked={selectedVendors.includes(v.id)}
                  onChange={() =>
                    setSelectedVendors((prev) =>
                      prev.includes(v.id)
                        ? prev.filter((id) => id !== v.id)
                        : [...prev, v.id]
                    )
                  }
                />
                <span className="vs-vendor-name">{v.name}</span>
              </label>
              <div className="vs-vendor-info">
                <div>Email: {v.email}</div>
                <div>Category: {v.category}</div>
              </div>
            </div>
          ))}
        </div>

        <button className="vs-send-btn" onClick={handleSendRfp}>
          Send RFP
        </button>
      </div>
    </div>
  );
}
