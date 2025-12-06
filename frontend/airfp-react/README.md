 AI RFP System (React)
This is the user interface for managing RFPs, vendors, AI recommendations, and dashboard.

Run Frontend
npm install
npm run dev

Runs at:
http://localhost:5173


Pages Included
Dashboard


Stats cards


Recent activity


AI recommendations


Create RFP
Upload PDF → extract → modify → save.
Vendor Selection
Pick RFP + Vendors → send email.
Vendor Responses
List all responses.
AI Recommendation
Select RFP → view best vendor + detailed ranking.
Vendor Management
Add vendor + list existing vendors.

Folder Structure
/src
   ├── pages/
   ├── services/api.js
   ├── components/
   ├── styles/
   ├── App.jsx
   └── main.jsx


API Configuration
src/services/api.js
import axios from "axios";

export default axios.create({
  baseURL: "http://localhost:8080/api",
});

