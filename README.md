# Secure Border Controller (SBC) for FLUIDOS

## Overview
The Secure Border Controller (SBC) is a component developed within the FLUIDOS EU project, designed to enforce dynamic network policies between federated Kubernetes clusters. The FLUIDOS platform implements the computing continuum by enabling the sharing of unutilized resources scattered across the edge within heterogeneous devices with seamless integration. In this context, upon workload offloading between different FLUIDOS nodes managed by Liqo, the SBC component automatizes the harmonization, translation and enforcement processes that bring high-level network configuration intents expressed by the consumer into network policies ensuring compatibility with the provider's restrictions and rules.
  
## Key Features
- Support for high-level communication intents
- Verification of the candidates potentially accommodating the consumer requests before peering and offloading
- Reconciliation of request intents expressed by the consumer and authorization intents expressed by the resources provider 
- Provision of an high-level language useful to define access and limitation intents referred to network communications
- Guarantee of coherence and correctness of network configurations in a FLUIDOS context, without manual intervention
 
## Repository Structure
- `controller-code/`: Core implementation of the SBC.
- `docs/`: Technical documentation and integration guides.
- `scripts/`: Helper scripts for automation and setup.

## Quick Start

## Demo Example

---
