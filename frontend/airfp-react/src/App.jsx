import { BrowserRouter, Routes, Route } from "react-router-dom";

import Sidebar from "./components/Sidebar";
import Header from "./components/Header";

import Dashboard from "./pages/Dashboard";
import CreateRfp from "./pages/CreateRfp";
import VendorSelection from "./pages/VendorSelection";

import VendorResponses from "./pages/VendorResponse";
import Recommendation from "./pages/Recommendation";
import RfpDetails from "./pages/RfpDetails";
import AddVendor from "./pages/AddVendor";
import RecommendationSelect from "./pages/RecommendationSelect";

import "./index.css";

export default function App() {
  return (
    <BrowserRouter>
      <Header />

      <div className="container">
        <Sidebar />
        

        <div className="main-content">
          <Routes>
            
            <Route path="/" element={<Dashboard />} />
            <Route path="/create-rfp" element={<CreateRfp />} />
            <Route path="/addvendors" element={<AddVendor />} />
            <Route path="/recommendation" element={<RecommendationSelect />} />
<Route path="/recommendation/:id" element={<Recommendation />} />


              <Route path="/rfp/:id" element={<RfpDetails />} />
            
            <Route path="/vendor-selection" element={<VendorSelection />} />
            <Route path="/vendor-responses" element={<VendorResponses />} />
            <Route path="/recommendation" element={<Recommendation />} />
          </Routes>
        </div>
      </div>
    </BrowserRouter>
  );
}
