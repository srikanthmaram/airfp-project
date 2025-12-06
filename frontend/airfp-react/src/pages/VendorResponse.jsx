import React, { useEffect, useState } from "react";
import {
  getAllRfps,
  getVendorResponsesByRfp,
} from "../services/api";

import "../styles/VendorResponses.css";

export default function VendorResponses() {
  const [rfps, setRfps] = useState([]);
  const [selectedRfp, setSelectedRfp] = useState(null);

  const [responses, setResponses] = useState([]);
  const [selectedResponse, setSelectedResponse] = useState(null);

  const [loading, setLoading] = useState(false);

  useEffect(() => {
    getAllRfps().then((res) => setRfps(res.data));
  }, []);

  const loadResponses = async (rfpId) => {
    setSelectedRfp(rfpId);
    setResponses([]);
    setSelectedResponse(null);
    setLoading(true);

    const res = await getVendorResponsesByRfp(rfpId);
    setResponses(res.data);
    setLoading(false);
  };

  return (
    <div className="vr-container">
      <div className="vr-header">
        <h2>Vendor Responses</h2>

        <select
          className="vr-select"
          onChange={(e) => loadResponses(e.target.value)}
        >
          <option>Select RFP</option>
          {rfps.map((r) => (
            <option key={r.id} value={r.id}>
              {r.title}
            </option>
          ))}
        </select>
      </div>

      <div className="vr-body">
        {/* Left Table */}
        <div className="vr-list">
          {loading ? (
            <div className="vr-loading">Loading responses...</div>
          ) : (
            <table className="vr-table">
              <thead>
                <tr>
                  <th>Vendor</th>
                  <th>Total Price</th>
                  <th>Delivery</th>
                  <th>Warranty</th>
                  <th>Received</th>
                  <th></th>
                </tr>
              </thead>

              <tbody>
                {responses.map((res) => {
                  const data = JSON.parse(res.normalizedJson);

                  return (
                    <tr key={res.id}>
                      <td>{data.vendor_name}</td>
                      <td>{data.total_price}</td>
                      <td>{data.delivery_timeline_days} days</td>
                      <td>{data.warranty}</td>
                      <td>{new Date(res.receivedDate).toLocaleString()}</td>
                      <td>
                        <button
                          onClick={() => setSelectedResponse(data)}
                          className="vr-view-btn"
                        >
                          View
                        </button>
                      </td>
                    </tr>
                  );
                })}
              </tbody>
            </table>
          )}
        </div>

        {/* Right Panel */}
        <div className="vr-right-panel">
          {selectedResponse ? (
            <>
              <h3>{selectedResponse.vendor_name}</h3>
              <p>Email: {selectedResponse.vendor_email}</p>

              <h4>Items</h4>
              <ul>
                {selectedResponse.items.map((i, idx) => (
                  <li key={idx}>
                    {i.item_name} — {i.quantity} pcs — ₹{i.total_price}
                  </li>
                ))}
              </ul>

              <h4>Attachments</h4>
              <ul>
                {selectedResponse.attachments.map((a, idx) => (
                  <li key={idx}>{a.filename}</li>
                ))}
              </ul>

              <details>
                <summary>Raw JSON</summary>
                <pre>{JSON.stringify(selectedResponse, null, 2)}</pre>
              </details>
            </>
          ) : (
            <div className="vr-placeholder">
              Select a vendor response to view details
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
