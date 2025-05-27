# Intent MSPL
This document describes the extension of the MSPL (Medium-level Security Policy Language) designed to support the expression of intents holding network configuration requirements for secure resource acquisition in the FLUIDOS continuum. This language is independent from the device-specific configuration syntax, providing a high-level and user-friendly abstraction for defining security policies. In particular, the intent language must be capable of expressing each actor's isolation requirements, which are differentiated based on their role. For this reason we designed multiple intent sets which are presented in the following section.

## Description of the different intent sets
***1. Private Intents***: These intents are used to define communications within a single virtual cluster, involving both local and remote resources. They are not subject to host-level authorization, beacuse users must have full control over the communication within their own resources, either local or offloaded to a remote provider. They allow fine-grained management of intra-virtual cluster communications, supporting the implementation of a _least privilege_ architecture.

***2. Request Intents***: These intents are used to define communications that cross the boundaries of a virtual cluster, involving both local and remote resources. Their purpose is to regulate access to services hosted by the provider or to external endpoints on the Internet. Moreover, special configuration parameters could be used to refine the isolation settings of consumer's workloads with respect to the provider, e.g. choosing to allow or not monitoring from the hosting provider.

***3. Authorization Intents***: These intents allow users to define policies that govern the communication between different virtual clusters within the same phisical cluster, under their administrative control. They are applied to all guests renting some resources of such physical cluster, and are comppose of two sub-sets:

- ***Denied Communication Intents***: They are used to block or filter network connectivity for all the hosted workloads, e.g. to block communications towards blacklisted URLs or to restrict access towards specific services under the provider's control. 

- ***Mandatory Communication Intents***: They are used to express network connectivity requirements enforced to all the hosted workloads. A common scenario is to enable provider-side monitoring services used for accounting and gathering telemetry data.

***4. Setup Intents***:
These intents hold essential networking configuration required for proper operation of the undelrying components. Within FLUIDOS, these are designed to be used to allow VPN communication established between peered clusters by Liqo. They are not intended to be used for application-level security policies, but rather for the underlying network setup.

## MSPL Schema
The MSPL structure has been extended to adapt it to the specific needs of this context. The following diagram partially illustrates the MSPL schema, focusing on the structure and relationships of the various elements composing a network intent, independently from the set it belongs to. The new elements are highlighted with green color. 
<p align="center">
	<img src="./images/MSPL_schema.svg" width="1000">
</p>

### KubernetesNetworkFiltering
This it the newly defined value for `CapabilityType` to be associated to a `Configuration` element within an `ITResource`. It is used to express network intents for the orchestrator responsible for border protection. 

### PrivateIntents
This is one of the three specializations of the `Configuration` element. In particular, this is used to define the set of _private_ _intents_. It inherits all base attributes and introduces a list of `ConfigurationRule` elements (zero or more), each representing a single intent. Even if not enforced in the XSD schema, all the intents in this set should be defined in whitelisting (i.e., implicit default action is DENY, and all the defined intents have action ALLOW).

### RequestIntents
This element has a similar structure to `PrivateIntents`, but targets cross-virtual cluster communications, i.e. interactions that traverse the boundary of the virtual cluster managed by the intent issuer. These are used to expres the set of _request_ _intents_. Differently from the `PrivateIntents` schema, it introduces an additional boolean attribute representing a configuration parameter available for the user, who can specify whether to allow or deny the creation of monitoring communication channels operated by the resource provider. 

### AuthorizationIntents
This is the last specialization of the `Configuration` element and it is designed to express the _authorization_ _intents_. This one contains two optional lists: 
- `ForbiddenConnectionList`: defines connections that must be denied for hosted workloads (e.g., blocking offloaded pods from accessing local services or the internet). All rules in this list carry the DENY action. Notice that in this context, the "*" symbol refers specifically to all offloaded pods, not the entire cluster.

- `MandatoryConnectionList` defines connections that must be allowed by hosted resources (e.g., opening a monitoring port). All rules in this list carry the ALLOW action.

### KubernetesNetworkFilteringAction
This is a specialization of the `ConfigurationAction` element. It specifies the action to be taken when a `ConfigurationRule` matches a condition. It contains a single attribute named `KubernetesNetworkFilteringActionType` which accepts only two possible values: `DENY` or `ALLOW`.

### ResourceSelector
This complex element defines the matching conditions for a  `ConfigurationRule`. It includes the following components used to identify a network communication channel:
- `source` and `destination`: both of type `ResourceSelector`, are used to specify the origin and destination of the network connection.
- `sourcePort` and `destinationPort`: specify the port(s) for the connection.
- `protocolType`: specifies the network protocol. Valid values are `TCP`, `UDP`, `STCP`, and `ALL`.

### CIDRSelector
Used to match source or destination resources based on IP ranges expressed in CIDR notation (e.g., `10.0.2.0/24`).


### PodNamespaceSelector
Used to match resources based on Kubernetes labels of both Pods and Namespaces. It contains two lists (`Pod` and `Namespace`), each composed of `KeyValue` elements representing label match conditions. All KeyValue pairs are interpreted in an "AND" fashion, meaning all conditions must be satisfied.


> **Notes**: 
>- An example of intent can be found in the [example file](./docs/MSPL_intent_example.xml).
>- Note that, namespaces can not be selected by name but need to be selected though an assigned label. However, this is not a limitation since by default, Kubernetes assigns to each created namespace a label which key is `kubernetes.io/metadata.name`  and value is indeed the namespace name. If the key field of a namespace is empty, this will be automatically interpreted as this by-default one.