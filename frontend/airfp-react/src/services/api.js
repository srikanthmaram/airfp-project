
import axios from "axios"

const API_BASE_URL = import.meta.env.VITE_API_URL;


const API = axios.create({
  baseURL:API_BASE_URL
});

export const extractRfp = (data) =>API.post("/rfp/extract", data)
export const createRfp = (data) => API.post("/rfp/create", data);
export const getRfp=(id)=>API.get(`/rfp/${id}`)

export const getAllRfps=()=>API.get("/rfp/rfps")
export const getAllVendors = () =>API.get("/vendor/vendors")

export const getSentRfps = () => API.get("/rfp/sent");
export const sendToVendors = (data) => API.post("/rfp/send", data);

export const fetchVendorResponses = (rfpId) =>
  API.get(`/vendor/response/${rfpId}`);

export const fetchRecommendation = (rfpId) =>
  API.get(`/vendor/recommend/${rfpId}`);





export const createVendor = (data) => API.post("/vendor/add", data);

export const sendRfpToVendors = (rfpId, vendorIds) => 
    API.post(`/vendor/${rfpId}/sendToVendors`, vendorIds); 


export const getVendorResponsesByRfp = (rfpId) =>
  API.get(`/vendor/vendor-responses/rfp/${rfpId}`);




export const getSpeechtoText=(data)=>API.post("/voice/transcribe",data)



// ----------------------------
// VENDOR RESPONSES
// ----------------------------

// Fetch all vendor responses for RFP
export const getVendorResponses = (rfpId) =>
  API.get(`/vendor-responses/rfp/${rfpId}`);


// ----------------------------
// SCORING & EVALUATION
// ----------------------------

// Get vendor evaluation scores for an RFP
export const getVendorScores = (rfpId) =>
  API.get(`/vendor-evaluation/${rfpId}`);


// ----------------------------
// EMAIL POLLING (optional triggers)
// ----------------------------

// Force manual poll (helpful during dev)
export const pollEmails = () => API.post("/email/poll-now");



export default API;
