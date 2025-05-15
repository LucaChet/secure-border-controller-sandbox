#export KUBECONFIG=../demo_da_presentare/node-main/tools/scripts/fluidos-consumer-1-config

# echo "[+] MANUAL step: update of the peeringCandidates (not properly reflected)."
# retrieve the provider node id and ip
# provider_node_id=$(kubectl get peeringCandidates -n fluidos -o jsonpath='{.items[0].spec.flavor.spec.owner.nodeID}')
# provider_ip=$(kubectl get peeringCandidates -n fluidos -o jsonpath='{.items[0].spec.flavor.spec.owner.ip}')

# kubectl delete peeringcandidates --all -n fluidos
# echo "[+] Deploying the custom controller"
# kubectl apply -f ./custom-controller.yaml

sleep 1 # to give time to the controller to spawn


# path_peeringCandidates="./peeringCandidates"
# Loop through all files in the directory
# for FILE in "$path_peeringCandidates"/*; do
    # Check if it's a file (not a directory)
#     if [ -f "$FILE" ]; then
#         echo "[+] Processing $FILE"
#         yq eval ".spec.flavor.spec.owner.nodeID = \"$provider_node_id\"" -i "$FILE"
#         yq eval ".spec.flavor.spec.owner.ip = \"$provider_ip\"" -i "$FILE"
#         yq eval ".spec.flavor.spec.providerID = \"$provider_node_id\"" -i "$FILE"
#     fi
# done

# echo ""
# echo "[+] Applying the modified peeringCandidates"
# kubectl apply -f $path_peeringCandidates

echo ""
echo "[+] Wait for the secure-border-controller to be ready."
pod_name=$(kubectl get pods -n fluidos )
kubectl wait --for=condition=Ready pod/$pod_name -n fluidos --timeout=300s


#echo ""
#sleep 6
echo "[+] Check border-protection-controller logs for VERIFY result."
# kubectl logs -n fluidos $pod_name


