# Java Project with SonarQube on Azure VM (Docker) + GitHub Actions CI/CD

This repository demonstrates how to:

1. Provision an Azure Virtual Machine
2. Install SonarQube on the VM using Docker Compose
3. Integrate SonarQube with a GitHub Actions CI/CD pipeline for a Java (Maven) project

By the end of this tutorial, every push to your repository will trigger:
- A Maven build
- A SonarQube scan
- Code quality reporting inside your self-hosted SonarQube instance

---

# Architecture Overview

```
GitHub Push
     ↓
GitHub Actions Workflow
     ↓
Maven Build
     ↓
SonarQube Scan
     ↓
Azure VM (Dockerized SonarQube + PostgreSQL)
     ↓
Code Quality Dashboard
```

---

# Prerequisites

Before starting, ensure you have:

- An active Azure subscription
- A GitHub repository with a Java Maven project
- SSH access capability
- Basic familiarity with Docker and GitHub Actions

Recommended VM size: **Minimum 4GB RAM**

---

# Step 1 — Create an Azure Virtual Machine

## Create VM in Azure Portal

1. Go to: https://portal.azure.com
2. Navigate to **Virtual Machines**
3. Click **Create → Azure Virtual Machine**
4. Configure the following:

| Setting | Value |
|----------|--------|
| Image | Ubuntu 22.04 LTS |
| Size | Standard B2s (or ≥4GB RAM) |
| Authentication | SSH Public Key |
| Public IP | Enabled |

5. Click **Review + Create**
6. Click **Create**

---

# Step 2 — Open Port 9000 for SonarQube

After VM creation:

1. Go to the VM → **Networking**
2. Click **Add inbound port rule**
3. Configure:

| Setting | Value |
|----------|--------|
| Source | Any |
| Protocol | TCP |
| Destination Port | 9000 |
| Action | Allow |
| Priority | 300 |

4. Click **Add**

SonarQube will use port **9000**.

---

# Step 3 — Connect to the VM via SSH

From your local machine:

```bash
ssh azureuser@<YOUR_VM_PUBLIC_IP>
```

---

# Step 4 — Install Docker & Docker Compose

Update system:

```bash
sudo apt update
sudo apt upgrade -y
```

Install Docker:

```bash
sudo apt install docker.io -y
```

Enable Docker:

```bash
sudo systemctl start docker
sudo systemctl enable docker
```

Install Docker Compose:

```bash
sudo apt install docker-compose -y
```

Verify:

```bash
docker --version
docker-compose --version
```

---

# Step 5 — Configure SonarQube with Docker Compose

Create project directory:

```bash
mkdir sonarqube
cd sonarqube
```

Create compose file:

```bash
nano docker-compose.yml
```

Paste the following:

```yaml
version: "3"

services:
  sonarqube:
    image: sonarqube:community
    container_name: sonarqube
    restart: unless-stopped
    ports:
      - "9000:9000"
    environment:
      SONAR_JDBC_URL: jdbc:postgresql://db:5432/sonar
      SONAR_JDBC_USERNAME: sonar
      SONAR_JDBC_PASSWORD: sonar
    volumes:
      - sonarqube_data:/opt/sonarqube/data
      - sonarqube_logs:/opt/sonarqube/logs
    depends_on:
      - db

  db:
    image: postgres:12
    container_name: sonar-db
    restart: unless-stopped
    environment:
      POSTGRES_USER: sonar
      POSTGRES_PASSWORD: sonar
    volumes:
      - postgresql:/var/lib/postgresql/data

volumes:
  sonarqube_data:
  sonarqube_logs:
  postgresql:
```

Save and exit.

---

# Step 6 — Start SonarQube

```bash
sudo docker-compose up -d
```

Check running containers:

```bash
sudo docker ps
```

View logs (optional):

```bash
sudo docker-compose logs -f
```

---

# Step 7 — Access SonarQube Web UI

Open browser:

```
http://<YOUR_VM_PUBLIC_IP>:9000
```

Default login:

```
Username: admin
Password: admin
```

You will be prompted to change the password.

---

# Step 8 — Generate SonarQube Access Token

Inside SonarQube:

1. Click **My Account**
2. Go to **Security**
3. Under *Tokens*, enter a name
4. Click **Generate**
5. Copy the token (you won’t see it again)

---

# Step 9 — Configure GitHub Repository Secrets

In your GitHub repo:

1. Go to **Settings → Secrets and Variables → Actions**
2. Add:

| Name | Value |
|------|--------|
| SONAR_HOST_URL | http://<YOUR_VM_PUBLIC_IP>:9000 |
| SONAR_TOKEN | <YOUR_GENERATED_TOKEN> |

---

# Step 10 — Create GitHub Actions Workflow

Create file:

```
.github/workflows/ci-sonarqube.yml
```

Add:

```yaml
name: Workflow for Maven Build & Sonar Server Scan

on:
  push:
    branches:
      - master

jobs:
  build:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout Code
        uses: actions/checkout@v4

      - name: Set Up JDK 17
        uses: actions/setup-java@v5
        with:
          distribution: 'temurin'
          java-version: '17'

      - name: Build and Test with Maven
        run: mvn -B clean verify

      - name: Scan with SonarQube Server
        uses: sonarsource/sonarqube-scan-action@v7
        with:
          projectBaseDir: .
          args: >
            -Dsonar.projectKey=antipodal
        env:
          SONAR_TOKEN: ${{ secrets.SONAR_TOKEN }}
          SONAR_HOST_URL: ${{ secrets.SONAR_HOST_URL }}
```

---

# Step 11 — Trigger the Pipeline

Commit and push:

```bash
git add .
git commit -m "Add SonarQube CI pipeline"
git push origin master
```

Navigate to:

```
GitHub → Actions tab
```

You should see:
- Maven build
- SonarQube scan
- Successful job completion

---

# Step 12 — View Results in SonarQube

Return to:

```
http://<YOUR_VM_PUBLIC_IP>:9000
```

You will now see:

- Code coverage
- Code smells
- Bugs
- Vulnerabilities
- Technical debt
- Quality gate status

---

# Optional: Stop SonarQube

```bash
sudo docker-compose down
```

---

# Final Result

You now have:

- Azure-hosted VM  
- Dockerized SonarQube + PostgreSQL  
- Automated GitHub Actions CI/CD  
- Continuous code quality inspection  

---

# Why This Setup?

Running SonarQube on your own Azure VM allows:

- Full control over configuration
- No SaaS limitations
- Internal/private repository scanning
- Custom Quality Profiles and Gates

---

# Conclusion

This guide demonstrates how to deploy SonarQube on Azure using Docker Compose and integrate it into a GitHub Actions workflow for Java projects.

You now have a complete DevOps pipeline with automated static code analysis.
