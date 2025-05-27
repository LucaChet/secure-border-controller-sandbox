# Secure Border Controller for intent-based border protection
<p align="center">
  <img src="./docs/images/Fluidos_logo.png" alt="Architecture Diagram"/>
</p>

## Overview
The Secure Border Controller (SBC), developed as part off the FLUIDOS EU project, delivers an intent-based border protection solution to secure dynamic workflows in the cloud-edge continuum. In this context, upon workload offloading between different FLUIDOS nodes, the secure border controller plays a critical role by automatically secure the resources before they are consumed. This solution involves several key features. The verification and harmonization processes ensure alignment between the consumer's security objectives and the provider's restrictions, automatically resolving any possible discordance. Following this, the  translation and enforcement processes refine the high-level network configuration intents defined by the consumer into low-level security primitives, i.e., Kubernetes Network Policies, while ensuring they remain compatible with the provider's restrictions and rules.
  
## Key Features
- Support for high-level communication intents
- Verification process filtering the candidates potentially accommodating the consumer requests before peering and offloading
- Reconciliation of Private and Request intents expressed by the consumer and Authorization intents expressed by the provider 
- Smart harmonization process to ensure coherence and correctness of network security configuration
- Automatized refinement and enforcement of container isolation based on users' intents

## Requirements
To deploy and use the Secure Border Controller, the following prerequisites must be satisfied:

- **Kubernetes Cluster**:  
  The SBC component must interact with an existing Kubernetes cluster.

- **FLUIDOS Node Installation**:  
  The SBC integrates with a working installation of the [FLUIDOS Node](https://fluidos-project.github.io/node/).

- **Container Network Interface (CNI)**:  
  The SBC relies on Kubernetes Network Policies for enforcing network isolation. Even if the design could be easily extended to multiple CNIs, only **Calico** is currentyl supported.

- **Intents Formulation**:
  The SBC is based on multiple intent sets, i.e., Private and Request intents from the consumer and on Authorization intents from the provider. These intents must adhere to the schema defined in the [intent documentation](./docs/intent-mspl.md).
  
## Quick Start
This section provides a high-level guide to integrate the SBC with an existing Kubernetes cluster running the FLUIDOS Node components.

### 1️⃣ Install a FLUIDOS Node
- Follow the official instructions to deploy a FLUIDOS Node on your cluster.

### 2️⃣ Deploy the Secure Border Controller
- Clone this repository:
```bash
  git clone https://github.com/netgroup-polito/secure-border-controller.git
  cd secure-border-controller
```
- Create Service Account for the custom controller:
```bash
kubectl create serviceaccount custom-controller -n fluidos
```
- Create all the needed roles/clusterRoles and bindings (TODO: add the necessary RBAC files for easy deployment)
- Deploy the SBC using its manifest
```bash
kubectl apply -f ./secure-border-controller.yaml
```
4️⃣ Run the Demo Example (Optional)
- You can test the integration with a demo provided in the `demo/` folder, by following the provided [demo-guide](./docs/demo-guide.md). A public recording showcasing the demo is also available [here](https://www.youtube.com/watch?v=7NBoORvkJ5U&t=34s).

## Papers
- F. Pizzato, D. Bringhenti, R. Sisto and F. Valenza, "*An intent-based solution for network isolation in Kubernetes,*" 2024 IEEE 10th International Conference on Network Softwarization (NetSoft), Saint Louis, MO, USA, 2024, pp. 381-386, doi: 10.1109/NetSoft60951.2024.10588939 [[link](https://ieeexplore.ieee.org/abstract/document/10588939)]
- F. Pizzato, D. Bringhenti, R. Sisto and F. Valenza, “*Workload isolation through intent-based network management for the future of computing continuum*”, under review in IEEE Internet Computing.
- F. Pizzato, D. Bringhenti, R. Sisto and F. Valenza, “*Intent-driven network isolation for the cloud computing continuum*”, under review in Journal of Network and Systems Management.

---
