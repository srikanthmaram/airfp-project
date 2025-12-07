AI-Powered RFP Automation System

A complete end-to-end procurement automation platform built using Spring Boot, React, and AI vendor evaluation.
This project extracts RFPs, sends them to vendors, collects responses, evaluates vendors using AI, and generates recommendations.

Tech Stack

Backend :
Spring Boot 3+
Java 17+
JPA + Hibernate
Database :
MS SQL Server datbase


LLM:
OpenRouter API (LLM)
Vosk Speech to Text offline Model
JavaMail (IMAP/SMTP)


Frontend :
React + Vite
Axios
React Router


Project Structure
/ai-rfp-system
   ├── backend/   (Spring Boot API, AI evaluation, DB)
   ├── frontend/  (React UI)
   └── README.md  (this file)
   

Features :

1. RFP Extraction

2. User Prompt → backend extracts title, items, budget, warranty, timeline, payment terms.

3. Send RFP to Vendors

4. Select RFP + Vendors → system emails RFP automatically.

5. Vendor Response Capture

6. Vendor emails are parsed automatically by IMAP polling.

7. AI Vendor Evaluation

8. LLM compares vendor responses and produces:

       Ranking,Scores,Strengths & weaknesses.

9. Final recommendation & Summary

10. Dashboard Shows:

  Total RFPs
  Sent RFPs
  Recent activity

11. Vendor Management
  Add/View vendors with details.



📦 Running the Project

This project has two applications:

1️⃣ Start Backend → instructions in /backend/README.md
2️⃣ Start Frontend → instructions in /frontend/README.md



🔐 Environment Variables & Secrets

All details are documented inside:

👉 backend/README.md

Secrets are not committed to GitHub.


Architecture Diagram:



![Architecture Diagram](./docs/architecture.png)



```
                                  ┌───────────────────────────────┐
                                  │       AIRfp (React App)       │
                                  │-------------------------------│
                                  │   • Dashboard                 │
                                  │   • Create RFP                │
                                  │   • Vendor Selection          │
                                  │   • Add Vendor                │
                                  │   • Vendor Response           │
                                  │   • AI Recommendations        │
                                  └───────────────┬───────────────┘
                                                  │
                                                  ▼
                      ┌────────────────────────────────────────────────────────┐
                      │                  airfp (Spring Boot App)               │
                      │--------------------------------------------------------│
                      │  • RfpExtractionService                                │
                      │  • VendorProposalService                               │
                      │  • VendorMessagingService                              │
                      │  • EmailPollingService                                 │
                      │  • EmailNormalizationService                           │
                      │  • EmailReceiverService                                │
                      │  • VendorEvaluationService                             │
                      │  • OpenRouterClient                                    │
                      │  • VoiceToTextService                                  │
                      └───────────────┬───────────────────────┬────────────────┘
                                      │                       │
                                      │                       │
                                      ▼                       ▼
                ┌────────────────────────────┐     ┌────────────────────────────┐
                │            LLMs            │     │      MS SQL SERVER         │
                │----------------------------│     │          Database           │
                │  • OpenRouterClient        │     └────────────────────────────┘
                │  • Vosk Speech to Text     │
                │     (Offline Model)        │
                └────────────────────────────┘



```




Author:

Srikanth Maram
Full-stack Java + React Developer
RFP Automation Project

