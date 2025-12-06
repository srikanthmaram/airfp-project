import { Link } from "react-router-dom";
import "../layout.css";

export default function Sidebar() {
  return (
    <div className="sidebar">
      <Link to="/">Dashboard</Link>
      <Link to="/create-rfp">Create RFP</Link>
      
      <Link to="/vendor-selection">Vendor Selection</Link>
      <Link to="/vendor-responses">Vendor Responses</Link>
      <Link to="/addvendors">Add Vendor</Link>
      <Link to="/recommendation">Recommendation</Link>
    </div>
  );
}
