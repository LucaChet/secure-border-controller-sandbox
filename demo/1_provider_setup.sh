#export KUBECONFIG=../demo_da_presentare/node-main/tools/scripts/fluidos-provider-1-config
echo " [+] In PROVIDER cluster..."
# Populate the provider cluster with the initial resources
echo " [+] Creating namespaces and deployments"
kubectl create namespace monitoring
kubectl create namespace handle-payments
kubectl label namespace monitoring name=monitoring
kubectl label namespace handle-payments name=handle-payments
kubectl apply -f ./deployments/provider

echo ""
sleep 2

# Deploy the custom controller
echo "[+] Creating Service Accont for the custom controller"
kubectl create serviceaccount custom-controller -n fluidos
echo "[+] Creating all the needed roles/clusterRoles and bindings"
kubectl apply -f ./serviceAccounts
echo "[+] Deploying the custom controller"
kubectl apply -f ./custom-controller.yaml

echo ""
sleep 2

################################
# MANUAL update of the flavors #
################################

# Extracting the provider node id and ip
echo "[+] MANUAL step: Pushing new flavors with the provider's AuthorizationIntents data"
provider_node_id=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.nodeID}')
provider_ip=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.ip}')
echo "[+] Provider's node id: $provider_node_id"
echo "[+] Provider's ip: $provider_ip"
echo "[+] Deleting all the old flavors"
kubectl delete flavors --all -n fluidos
echo "[+] Creating new flavors with AuthorizationIntents data"
path_flavors="./flavors"
# Loop through all files in the directory
for FILE in "$path_flavors"/*; do
    # Check if it's a file (not a directory)
    if [ -f "$FILE" ]; then
        echo "[+] Processing $FILE"
        yq eval ".spec.owner.nodeID = \"$provider_node_id\"" -i "$FILE"
        yq eval ".spec.owner.ip = \"$provider_ip\"" -i "$FILE"
        yq eval ".spec.providerID = \"$provider_node_id\"" -i "$FILE"  
    fi
done
echo "[+] Applying the new flavors"
kubectl apply -f $path_flavors

#########################
#   end of MANUAL step  #
#########################