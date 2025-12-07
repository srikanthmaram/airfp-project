 AI RFP System (React)
This is the user interface for managing RFPs, vendors, AI recommendations, and dashboard.

Run Frontend:

npm install
npm run dev

Runs at:
http://localhost:5173


Pages Included

Dashboard :
  Stats cards
  Recent activity

Create RFP :
  User Prompt->Extract RFP using LLM->JSON Preview + Form  Preview->save to database
  Voice record-> Vosk (Speech to Text) -> Prompt -> Extract RFP -> JSON+Form Review -> save to database

Vendor Selection :
      Pick RFP + Select Vendors → send RFP via email.

Vendor Responses :
   1. List all responses.
   2. Pick one Vendor Response
   3. AI Recommendation + score + summary

   Select RFP → view best vendor + detailed ranking.

Vendor Management :
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


