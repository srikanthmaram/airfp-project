
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import "../styles/RfpDetails.css";
import { getRfp } from "../services/api";

export default function RfpDetails() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [rfp, setRfp] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchRfp();
  }, []);

  const fetchRfp = async () => {
    try {
      const res = await getRfp(id)
      const data = res.data
      setRfp(data);
    } catch (err) {
      console.error(err);
    }
    setLoading(false);
  };

  if (loading) return <p>Loading...</p>;
  if (!rfp) return <p>RFP not found</p>;

  return (
    <div className="rfp-details-container">

      
      <div className="rfp-header-card">
        <h1>{rfp.title}</h1>
        <p className="desc">{rfp.description}</p>

        <div className="rfp-info-grid">
          <div><strong>Budget:</strong> {rfp.budget || "N/A"}</div>
          <div><strong>Delivery Days:</strong> {rfp.deliveryTimelineDays}</div>
          <div><strong>Warranty:</strong> {rfp.warranty || "N/A"}</div>
          <div><strong>Payment Terms:</strong> {rfp.paymentTerms || "N/A"}</div>
        </div>

        <div className="rfp-actions">
          
          <button onClick={() => navigate(`/vendor-selection`)}>Send to Vendors</button>
          <button onClick={() => navigate(`/rfp/${id}/responses`)}>View Responses</button>
          <button onClick={() => navigate(`/rfp/${id}/evaluate`)}>Evaluate Vendors</button>
        </div>
      </div>

      
      <div className="items-section">
        <h2>Items</h2>

        <table className="items-table">
          <thead>
            <tr>
              <th>Name</th>
              <th>Quantity</th>
              <th>Specs</th>
            </tr>
          </thead>

          <tbody>
            {rfp.items.map((it) => (
              <tr key={it.id}>
                <td>{it.itemName}</td>
                <td>{it.quantity}</td>
                <td>
                  {Object.entries(JSON.parse(it.specsJson || "{}")).map(
                    ([k, v]) => (
                      <div key={k}>{k}: {v}</div>
                    )
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

    </div>
  );
}
