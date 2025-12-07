
import React, { useState ,useEffect} from "react";
import { createVendor,getAllVendors } from "../services/api";
import "../styles/AddVendor.css";

export default function AddVendor() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    phone: "",
    category: "",
  });
const [vendors, setVendors] = useState([]);
const [loadingVendors, setLoadingVendors] = useState(true);

useEffect(() => {
  loadVendors();
}, []);

const loadVendors = async () => {
  try {
    const res = await getAllVendors();
    setVendors(res.data);
  } catch (e) {
    console.error("Failed to load vendors", e);
  } finally {
    setLoadingVendors(false);
  }
};

  const [status, setStatus] = useState(null); 

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setStatus("loading");

    try {
      await createVendor(form);
      setStatus("success");
      setForm({ name: "", email: "", phone: "", category: "" });
    } catch (err) {
      console.error(err);
      setStatus("error");
    }
  };

  return (
    <>
    <div className="vendor-container">
      <h2 className="vendor-title">Add New Vendor</h2>

      <form className="vendor-form" onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Vendor Name *</label>
          <input
            type="text"
            name="name"
            value={form.name}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Email *</label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Phone</label>
          <input
            type="text"
            name="phone"
            value={form.phone}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>Category</label>
          <select name="category" value={form.category} onChange={handleChange}>
            <option value="">Select Category</option>
            <option value="IT">IT</option>
            <option value="Furniture">Furniture</option>
            <option value="Construction">Construction</option>
            <option value="Software">Software</option>
            <option value="Electrical">Electrical</option>
            <option value="General">General</option>
          </select>
        </div>

        <button className="vendor-submit-btn" type="submit" disabled={status === "loading"}>
          {status === "loading" ? "Saving..." : "Save Vendor"}
        </button>

        {status === "success" && (
          <div className="success-message">
            Vendor added successfully!
          </div>
        )}

        {status === "error" && (
          <div className="error-message">
            Failed to add vendor. Try again.
          </div>
        )}
      </form>
      

      
    </div>
    
<div className="vendor-list-container">
  <h3 className="vendor-list-title"> Vendors List</h3>

  {loadingVendors ? (
    <p>Loading vendors...</p>
  ) : (
    <table className="vendor-table">
      <thead>
        <tr>
          <th>Name</th>
          <th>Email</th>
          <th>Phone</th>
          <th>Category</th>
        </tr>
      </thead>
      <tbody>
        {vendors.length === 0 ? (
          <tr>
            <td colSpan="4" style={{ textAlign: "center", padding: "10px" }}>
              No vendors found.
            </td>
          </tr>
        ) : (
          vendors.map((v) => (
            <tr key={v.id}>
              <td>{v.name}</td>
              <td>{v.email}</td>
              <td>{v.phone || "-"}</td>
              <td>{v.category || "-"}</td>
            </tr>
          ))
        )}
      </tbody>
    </table>
  )}
</div>
    </>
    
  );
}
