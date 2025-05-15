# Secure Border Controller for FLUIDOS

## Overview
The Secure Border Controller is a component developed within the FLUIDOS EU project, designed to enforce dynamic network policies within the computing continuum. In this context, upon workload offloading between different FLUIDOS nodes managed by Liqo, the secure border controller automatizes the verification, harmonization, translation and enforcement processes. These bring high-level network configuration intents expressed by the consumer into network policies ensuring compatibility with the provider's restrictions and rules.
  
## Key Features
- Support for high-level communication intents
- Verification of the candidates potentially accommodating the consumer requests before peering and offloading
- Reconciliation of Private and Request intents expressed by the consumer and Authorization intents expressed by the resources provider 
- Guarantee of coherence and correctness of network configurations in a FLUIDOS context, without manual intervention
- Automatized management of container isolation

## Requirements
To deploy and use the Secure Border Controller, the following prerequisites must be satisfied:

- **FLUIDOS Node Installation**:  
  The SBC integrates with a working installation of a **FLUIDOS Node**. You can find the FLUIDOS Node repository here: [https://github.com/FLUIDOS-Project/fluidos-node](https://fluidos-project.github.io/node/).

- **Kubernetes Cluster**:  
  The SBC component interact with a Kubernetes cluster either acting as consumer or as provider.

- **Container Network Interface (CNI)**:  
  The SBC relies on Kubernetes Network Policies for intent enforcement. Currently, only **Calico** is fully supported and tested as the CNI plugin.
  
> **Note**: Support for additional CNI providers may be considered in future iterations.

- **Intents Formulation**:
  The SBC is based on Private and Request intents from the consumer and on Authorization intents from the provider. They must be formulated as defined in the [intent documentation](./docs/intent-mspl.md).
  
## Quick Start
This section provides a high-level guide to integrate the SBC with an existing FLUIDOS Node deployment.

### 1️⃣ Install a FLUIDOS Node
- Follow the official instructions to deploy a FLUIDOS Node on your cluster

### 2️⃣ Deploy the Secure Border Controller
- Clone this repository:
```bash
  git clone https://github.com/REPO_NAME.git
  cd secure-border-controller
```
- Create Service Account for the custom controller:
```bash
kubectl create serviceaccount custom-controller -n fluidos
```
- Create all the needed roles/clusterRoles and bindings
- Deploy the SBC using its manifest
```bash
kubectl apply -f ./custom-controller.yaml
```
4️⃣ Run the Demo Example (Optional)
- You can test the integration with a demo workflow provided in the `demo/` folder, by following the guide available at `docs/demo-guide.md`

## Repository Structure
- `controller-code/`: Core implementation of the SBC.
- `docs/`: Technical documentation and guides.
- `demo/`: Implementation of a demo workflow

---
