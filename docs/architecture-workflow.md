# Secure Border Controller - Architecture & Workflow

## Overview

The **Secure Border Controller (SBC)** is a component developed within the **FLUIDOS** European project. Its primary goal is to dynamically enforce network policies between federated Kubernetes clusters, in scenarios where resources are offloaded from a consumer to a provider.

This document provides a structured overview of the **logical architecture** and the **operational workflow** of the SBC, as integrated in the FLUIDOS framework.

---

## FLUIDOS Architecture: Context & Interactions

### Goal
A **consumer** intends to offload certain services to a **provider**. To enable this:
- The provider defines **authorization policies** (intents) using custom Kubernetes resources called **Flavors**. These are constraints the consumer must comply with.
- The consumer describes its connectivity requirements through a **ConfigMap** containing its **network intents**. These express the communication configuration required by the consumer functionalities.

### Involved Components

| Component               | Description                                                                                           |
|-------------------------|-------------------------------------------------------------------------------------------------------|
| **Flavor (CRD)**         | Authorization intents defined by the provider (resource quotas, mandatory or denied communication channels). |
| **Solver (CRD)**         | Analyzes provider offers (Flavors) and finds optimal peering candidates based on consumer needs and constraints. |
| **Verifier**             | Validates peering candidates, ensuring they comply with consumer intents.                             |
| **Liqo**                 | Manages the namespace offloading from the consumer to the provider cluster.                            |
| **Contract (CRD)**       | Defines all agreed details between consumer and provider, including resource allocations and reference to the intents.  |
| **SBC - Harmonizer** | Resolves the conflicting intents expressed by the consumer and provider. The latter can deny some requests and eventually force unsolicited communications  |
| **SBC - Translator** | Takes as input the harmonized intent set and produces the corresponding ingress/egress K8s Network Policies |

---

## Offloading & Network Policy Enforcement Workflow

### 1️⃣ Definition of Intents
- The **provider** defines its own **Flavors** describing resource and network authorization policies.
- The **consumer** defines a **ConfigMap** containing its desired **network intents**.

### 2️⃣ Provider Selection (Solver & Verifier)
- The **Solver** evaluates available providers, selecting the best peering candidates.
- The **Verifier** performs a final compliance check on selected candidates, ensuring network intents compatibility.

### 3️⃣ Contract Creation & Resource Allocation
- A **Contract (CRD)** is created, which includes:
  - Allocated resources (CPU, memory, pods).
  - The name of the **ConfigMap** containing the consumer's network intents, referenced via the **networkRequests** field.
- This process leads to a **Reservation** and subsequently an **Allocation** of resources.

### 4️⃣ Offloading with Liqo
- The consumer's namespace is offloaded to the provider cluster using **Liqo**.
- The provider now hosts consumer pods within its infrastructure.

### 5️⃣ Secure Border Controller Activation
- The SBC detects the offloaded namespace.
- It reads the corresponding **Contract** to retrieve the **networkRequests** field, which points to the consumer's ConfigMap with network intents.
- The SBC fetches and parses these intents for processing.

### 6️⃣ Intent Harmonization
- Consumer intents may **conflict** with the provider's authorization policies.
- The SBC includes a **Harmonizer Module** that:
  - Compares consumer and provider intents.
  - Resolves conflicts by applying provider-defined precedence.
  - Filters or modifies intents as necessary to ensure compliance.

### 7️⃣ Translation & Application of Network Policies
- The harmonized intents are **translated** into Kubernetes **NetworkPolicy** resources.
- The SBC dynamically applies these NetworkPolicies within the provider cluster.
- This guarantees security and isolation of consumer workloads, respecting the established agreements.

---

## Secure Border Controller Internal Modules

### Harmonizer
- Compares consumer intents against provider authorization policies.
- Resolves discrepancies by enforcing provider-defined rules.
- Produces a conflict-free set of intents for policy translation.

### Translator
- Converts harmonized intents into valid Kubernetes **NetworkPolicy** resources.
- Supports various network protocols (TCP, UDP, SCTP) and IP selectors (CIDR ranges).

### SBC Controller
- Coordinates the detection of offloaded namespaces.
- Manages Contract reading, intent retrieval, harmonization, translation, and policy application.

---

[(imgs/workflow_description.png)]
---

## Related Documents
- [docs/integration-guide.md](integration-guide.md)
- [examples/demo-guide.md](../examples/demo-guide.md)
