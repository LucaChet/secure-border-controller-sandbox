# Protected Borders – Demo Guide
<p align="center">
  <a href="https://www.youtube.com/watch?v=7NBoORvkJ5U&t=34s" target="_blank">
    <img src="./images/demo_cover.png" alt="Watch the demo" width="600"/>
  </a>
</p>

This document provides a guided walkthrough of the demo workflow showcasing the **Secure Border Controller**.

> **Note:** The demo requires an installation of the [FLUIDOS Node](https://github.com/fluidos-project/node) with a supported CNI. At the moment, only **Calico** is supported. Please refer to the FLUIDOS Node documentation for base setup instructions.

---

## Demo Setup – Step-by-Step

### 1️⃣ Setup the Environment

- Clone the FLUIDOS Node repository and go to its `tools/scripts` folder.
- Execute the environment setup script:
```bash
cd node/tools/scripts/
./setup.sh
```
- Choose default settings (1, then confirm with y where prompted).
- This will deploy two KinD clusters: Consumer and Provider, with Calico installed.

### 2️⃣ Configure Provider Flavors
- Extract Provider information:
```bash
export KUBECONFIG=fluidos-provider-1-config
kubectl get cm fluidos-network-manager-identity -n fluidos -o yaml
```
- Copy:
  - `domain`
  - `ip`
  - `nodeID`

- Edit the YAML files in `demo/flavors/`, updating:
  - `ProviderID` with `nodeID`
  - `domain` and `ip` fields

> This is automated in the demo by `./demo/1_provider_setup.sh`.

### 3️⃣ Run Initial Setup Scripts

- From the `demo/` folder:

```bash
# On the Consumer cluster
./0_consumer_setup.sh

# On the Provider cluster
./1_provider_setup.sh
```
- These scripts:
  - Set up namespaces and pods both uin the Consumer and Provider clusters
  - Create the necessary Service Accounts
  - Deploy the controller
  - In the Provider, Flavors are updated

  ### 4️⃣ Trigger the Peering Process

- Apply the Solver CR on the Consumer cluster:

```bash
kubectl apply -f ./demo/solver-custom.yaml
```
- After this command, REAR processed the request and the Provider's Flavor should have been received by the Consumer in the form of PeeringCandidate. Once this happen, the protected-border controller starts a Verification phase to check the compatibility of the received Flavor with the requested one. 
- Check controller logs to monitor the verification process:

```bash
kubectl get pods -n fluidos
kubectl logs <consumer-controller-pod> -n fluidos
```
> Wait ~20 seconds after applying the CR.

### 5️⃣ Reservation and Allocation
- Given the result of the verification, one of the received PeeringCandidate needs to be selected to proceed with the reservation and acquisition process.
- From the Consumer side:

```bash
./demo/3_reservation_and_allocation.sh <peeringCandidate-name>
```

- The script:
  - Updates `reservation.yaml` with information from ConfigMaps (Consumer details) and PeeringCandidate (Provider details)
  - Applies Reservation and Allocation CRs

  ### 6️⃣ Patch the Contract
- To pass the Consumer's Request intents to the Provider, the demo uses a Kubernetes ConfigMap that is automatically reflected on both clusters thanks to Liqo. 
- On the Provider cluster:

```bash
./demo/4_patch_contract.sh
```

- This:
  - Identifies the relevant `Contract`
  - Injects the name of a shared ConfigMap
  - Creates the ConfigMap containing Request intents

### 7️⃣ Resource Offloading and Harmonization
- The Consumer offloads its resources on the peered cluster and the Secure Border Controller on the Provider will perform harmonization, translation and enforcement of the proper intens (starting from the Requested intents in the ConfigMap, and the Authorization intents of the selected Flavor).
- On the Consumer cluster:

```bash
./demo/5_harmonize.sh
```

- Once the namespace is offloaded, the Provider’s controller will:
  - Read the `Contract`
  - Harmonize Request and Authorization intents
  - Translate them into Kubernetes Network Policies

- You can check logs of the secure border controller to verify Network Policies on the Provider:

```bash
kubectl logs <provider-controller-pod> -n fluidos
kubectl get networkpolicies -A
```
---

## Testing Network Policies

### 1️⃣ Access Pod Shell

```bash
kubectl exec -it <pod-name> -n <namespace> -- /bin/sh
```

### 2️⃣ Test a Connection

```bash
wget <destination-pod-ip>:<port>
```
---

## Cleanup
- To clean up the demo, run the script in the `tools/scripts` of the FLUIDOS Node repository:

```bash
cd node/tools/scripts/
./cleanup.sh
```