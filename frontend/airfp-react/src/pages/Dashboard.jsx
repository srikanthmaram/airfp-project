import React, { useEffect, useState } from "react";
import axios from "../services/api";
import "../styles/Dashboard.css";
import { useNavigate } from "react-router-dom";

export default function Dashboard() {
  const navigate = useNavigate();

  // ---------- SAFE DEFAULTS (prevents crashes) ----------
  const [stats, setStats] = useState({
    totalRfps: 0,
    sentRfps: 0,
    responsesReceived: 0,
    recommendationsReady: 0,
  });

  const [recent, setRecent] = useState([]);
  const [recommendations, setRecommendations] = useState([]);
  const [loading, setLoading] = useState(true);

  // ---------- LOAD DATA ----------
  useEffect(() => {
    async function loadData() {
      try {
        const res = await axios.get("/dashboard/summary");
        const data = res.data || {};

        // Defensive assignments ensure UI never crashes
        setStats({
          totalRfps: data.stats?.totalRfps ?? 0,
          sentRfps: data.stats?.sentRfps ?? 0,
          responsesReceived: data.stats?.responsesReceived ?? 0,
          recommendationsReady: data.stats?.recommendationsReady ?? 0,
        });

        setRecent(data.recentActivity ?? []);
        setRecommendations(data.recommendations ?? []);
      } catch (error) {
        console.error("Dashboard load error", error);
      } finally {
        setLoading(false);
      }
    }

    loadData();
  }, []);

  // ---------- LOADING UI ----------
  if (loading) return <div className="dash-loading">Loading dashboard...</div>;

  return (
    <div className="dash-container">

      {/* -------- HEADER -------- */}
      <h2 className="dash-title">Dashboard Overview</h2>

      {/* -------- STATS CARDS -------- */}
      <div className="dash-card-row">
        <div className="dash-card">
          <p className="num">{stats.totalRfps}</p>
          <p className="label">Total RFPs</p>
        </div>

        <div className="dash-card">
          <p className="num">{stats.sentRfps}</p>
          <p className="label">Sent to Vendors</p>
        </div>

        <div className="dash-card">
          <p className="num">{stats.responsesReceived}</p>
          <p className="label">Vendor Responses</p>
        </div>

        
      </div>

      {/* -------- QUICK ACTIONS -------- */}
      <div className="quick-actions">
        <button onClick={() => navigate("/create-rfp")}>+ Create RFP</button>
        <button onClick={() => navigate("/vendor-selection")}>📤 Send RFP</button>
        <button onClick={() => navigate("/recommendation")}>🤖 AI Recommendations</button>
        <button onClick={() => navigate("/addvendors")}>👥 Manage Vendors</button>
      </div>

      {/* -------- RECENT ACTIVITY -------- */}
      <h3 className="section-title">Recent Activity</h3>

      <div className="recent-list">
        {recent.length === 0 && <p>No recent activity.</p>}

        {recent.map((r, idx) => (
          <div key={idx} className="recent-item">
            <span className="dot"></span>
            <div>
              <p className="recent-text">{r.text}</p>
              <p className="recent-date">{r.time}</p>
            </div>
          </div>
        ))}
      </div>

      
     


    </div>
  );
}
