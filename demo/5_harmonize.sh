echo " [+] In CONSUMER cluster..."
# Populate the consumer cluster with the offloaded resources
echo " [+] Creating new namespaces and offload them."
kubectl create namespace payments
kubectl create namespace products
kubectl label namespace payments name=payments
kubectl label namespace products name=products

# Offload the namespaces
liqoctl offload namespace payments --pod-offloading-strategy Remote
liqoctl offload namespace products --pod-offloading-strategy Remote
# Deploy the offloaded resources
kubectl apply -f ./deployments/consumer/offload
# Deploy the configMap with intents
kubectl apply -f ./deployments/consumer/request_intents_map.yaml

echo ""
sleep 3

#echo "[+] In PROVIDER cluster..."
#export KUBECONFIG=../demo_da_presentare/node-main/tools/scripts/fluidos-provider-1-config

#echo "[+] Check border-protection-controller logs for HARMONIZE+TRANSLATION result."
#sleep 3
#pod_name=$(kubectl get pods -n fluidos | grep ^secure-border-controller | awk '{print $1}')
#kubectl logs -n fluidos $pod_name
