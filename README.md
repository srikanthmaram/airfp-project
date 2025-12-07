AI-Powered RFP Automation System

A complete end-to-end procurement automation platform built using Spring Boot, React, and AI vendor evaluation.
This project extracts RFPs, sends them to vendors, collects responses, evaluates vendors using AI, and generates recommendations.

Tech Stack

Backend :

Spring Boot 3+

Java 17+

SQL Server

JPA + Hibernate

OpenRouter API (LLM)

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
   

Features
RFP Extraction

User Prompt → backend extracts title, items, budget, warranty, timeline, payment terms.

Send RFP to Vendors

Select RFP + Vendors → system emails RFP automatically.

Vendor Response Capture

Vendor emails are parsed automatically by IMAP polling.

AI Vendor Evaluation

LLM compares vendor responses and produces:

Ranking

Scores

Strengths & weaknesses

Final recommendation

Summary

Dashboard Shows:

Total RFPs

Sent RFPs

Responses

Vendors

Recent activity



Vendor Management

Add/View vendors with details.


How to Run
Backend
cd backend
./mvnw spring-boot:run

Frontend
cd frontend
npm install
npm run dev

Secrets Management (IMPORTANT)

Store sensitive credentials like:

DB URL

EMAIL SMTP credentials

IMAP configuration

OpenRouter API Key





Architecture Diagram:
flowchart TB

subgraph FRONTEND["Frontend (React Application)"]
    A1[Dashboard]
    A2[Create RFP]
    A3[Vendor Selection]
    A4[Vendor Responses]
    A5[AI Recommendations]
    A6[Vendor Management]
end

A1 -->|Axios REST Calls| B
A2 -->|Axios REST Calls| B
A3 -->|Axios REST Calls| B
A4 -->|Axios REST Calls| B
A5 -->|Axios REST Calls| B
A6 -->|Axios REST Calls| B

subgraph BACKEND["Backend (Spring Boot)"]
    subgraph Controllers
        B1[RfpController]
        B2[VendorController]
        B3[EvaluationController]
        B4[DashboardController]
    end
    subgraph Services
        C1[RfpService]
        C2[VendorEmailService]
        C3[ImapPollingService]
        C4[VendorEvaluationService]
        C5[VendorDaoService]
    end
    subgraph Repositories
        D1[RfpRepository]
        D2[VendorRepository]
        D3[VendorResponseRepository]
        D4[VendorSentRecordRepository]
        D5[RecommendationResultsRepo]
    end
end

B --> C1
B --> C2
B --> C3
B --> C4
B --> C5

C1 --> D1
C2 --> D4
C3 --> D3
C4 --> D5
C5 --> D2

subgraph DB["SQL Server Database"]
    DB1[(RFP)]
    DB2[(RFP_ITEMS)]
    DB3[(VENDORS)]
    DB4[(VENDOR_SENT)]
    DB5[(VENDOR_RESPONSE)]
    DB6[(RECOMMENDATION_RESULT)]
    DB7[(VENDOR_RANKING)]
end

D1 --> DB1
D2 --> DB3
D3 --> DB5
D4 --> DB4
D5 --> DB6

subgraph AI["AI Evaluation Engine (OpenRouter LLM)"]
    AI1[Generate Vendor Ranking<br>Score, Strengths, Weaknesses]
end

C4 -->|LLM Prompt| AI1
AI1 -->|JSON Ranking Response| C4



Author:

Srikanth Maram
Full-stack Java + React Developer
RFP Automation Project

