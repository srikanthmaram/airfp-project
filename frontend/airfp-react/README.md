 AI RFP System (React)
This is the user interface for managing RFPs, vendors, AI recommendations, and dashboard.


### IMPORTANT HOW TO RUN THE APPLICAITON - START ###

Run Frontend:

Step1 : Inside the AIRFP-REACT project root, create a file named:
           .env
        Add the following environment variable
           VITE_API_URL=http://localhost:8080/api


Step2: npm install

Step3: npm run dev

App will start on http://localhost:5173/

### IMPORTANT HOW TO RUN THE APPLICAITON -- END ###


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


