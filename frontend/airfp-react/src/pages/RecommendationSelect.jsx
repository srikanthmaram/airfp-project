import React, { useEffect, useState } from "react";
import { getAllRfps } from "../services/api";
import { useNavigate } from "react-router-dom";
import "../styles/RecommendationSelect.css";

export default function RecommendationSelect() {
  const [rfps, setRfps] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    async function fetchRfps() {
      try {
        const res = await getAllRfps()
        setRfps(res.data);
      } finally {
        setLoading(false);
      }
    }
    fetchRfps();
  }, []);

  if (loading) return <div className="sel-loading">Loading RFPs...</div>;

  return (
    <div className="sel-container">
      <h2>Select an RFP for Recommendation</h2>

      <div className="sel-grid">
        {rfps.map((rfp) => (
          <div key={rfp.id} className="sel-card">
            <h3>{rfp.title}</h3>
            <p className="desc">{rfp.description}</p>

            <div className="info">
              <p><strong>Budget:</strong> {rfp.budget || "N/A"}</p>
              <p><strong>Delivery:</strong> {rfp.deliveryTimelineDays} days</p>
            </div>

            <button
              className="sel-btn"
              onClick={() => navigate(`/recommendation/${rfp.id}`)}
            >
              View AI Recommendation →
            </button>
          </div>
        ))}
      </div>
    </div>
  );
}
