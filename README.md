AI-Powered RFP Automation System

A complete end-to-end procurement automation platform built using Spring Boot, React, and AI vendor evaluation.
This project extracts RFPs, sends them to vendors, collects responses, evaluates vendors using AI, and generates recommendations.

Tech Stack
Backend

Spring Boot 3+

Java 17+

SQL Server

JPA + Hibernate

OpenRouter API (LLM)

JavaMail (IMAP/SMTP)

Quartz Scheduler (optional)

HikariCP

Lombok

Frontend

React + Vite

Axios

Tailwind / custom CSS

React Router

Project Structure
/ai-rfp-system
   ├── backend/   (Spring Boot API, AI evaluation, DB)
   ├── frontend/  (React UI)
   └── README.md  (this file)

Features
RFP Extraction

Upload a PDF → backend extracts title, items, budget, warranty, timeline, payment terms.

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

Dashboard

Shows:

Total RFPs

Sent RFPs

Responses

Vendors

Recent activity

Latest AI recommendations

Vendor Management

Add/edit vendors with company details.

🛠 How to Run
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

In a .env file (NOT committed to GitHub)

Author:

Srikanth Maram
Full-stack Java + React Developer
RFP Automation Project – Submission Ready

