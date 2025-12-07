import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import axios from "../services/api";
import "../styles/Recommendation.css";

export default function Recommendation() {
  const { id } = useParams(); // RFP id
  const [loading, setLoading] = useState(true);
  const [recommendation, setRecommendation] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    async function fetchData() {
      try {
        const res = await axios.get(`/rfp/${id}/evaluation`);
        setRecommendation(res.data);
      } catch (err) {
        setError("Could not load recommendation.");
      } finally {
        setLoading(false);
      }
    }
    fetchData();
  }, [id]);

  if (loading) return <div className="rec-loading">Fetching AI Recommendation...</div>;
  if (error) return <div className="rec-error">{error}</div>;
  if (!recommendation) return <div>No data available.</div>;

  const { summary, best_vendor_id, rankings } = recommendation;
  const bestVendor = rankings.find(v => v.vendor_response_id === best_vendor_id);

  return (
    <div className="rec-container">

      
      <h2 className="rec-title">AI Vendor Recommendation</h2>
      <p className="rec-summary-text">{summary}</p>

      
      <div className="best-card">
        <h3>Recommended Vendor</h3>
        <p className="best-vendor-name">
          {bestVendor.vendor_name || "Unnamed Vendor"}
        </p>
        <div className="score-pill">Score: {bestVendor.score}/10</div>
        <p className="justification">{bestVendor.justification}</p>
      </div>

      
      <h3 className="section-header">Vendor Comparison</h3>

      <div className="comparison-grid">
        {rankings.map((v) => (
          <div key={v.vendor_response_id} className="vendor-compare-card">
            <div className="vc-header">
              <strong>{v.vendor_name || "Unnamed Vendor"}</strong>
              <span className="score">Score: {v.score}</span>
            </div>

            <div className="vc-section">
              <div className="vc-subtitle">Strengths</div>
              <ul>
                {v.strengths.map((s, i) => <li key={i}>✓ {s}</li>)}
              </ul>
            </div>

            <div className="vc-section">
              <div className="vc-subtitle">Weaknesses</div>
              <ul>
                {v.weaknesses.map((w, i) => <li key={i}>✗ {w}</li>)}
              </ul>
            </div>
          </div>
        ))}
      </div>

      
      <h3 className="section-header">Detailed Evaluations</h3>

      {rankings.map((v) => (
        <div key={v.vendor_response_id} className="detail-card">
          <div className="detail-header">
            <h4>{v.vendor_name || "Unnamed Vendor"}</h4>
            <span className="score-large">{v.score}/10</span>
          </div>

          <div className="score-bar">
            <div className="score-fill" style={{ width: `${v.score * 10}%` }}></div>
          </div>

          <div className="info-row">
            <div>
              <h5>Strengths</h5>
              <ul>{v.strengths.map((s, i) => <li key={i}>✓ {s}</li>)}</ul>
            </div>
            <div>
              <h5>Weaknesses</h5>
              <ul>{v.weaknesses.map((w, i) => <li key={i}>✗ {w}</li>)}</ul>
            </div>
          </div>

          <p className="justification">{v.justification}</p>
        </div>
      ))}
    </div>
  );
}
