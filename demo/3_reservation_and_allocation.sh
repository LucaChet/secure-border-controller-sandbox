#export KUBECONFIG=../demo_da_presentare/node-main/tools/scripts/fluidos-consumer-1-config

if [ -z "$1" ]; then
  echo "Usage: $0 <peeringCandidate-name>"
  exit 1
fi

peeringcandidate=$1
echo "[+] MANUAL step: update of the Reservation CRD (PeeringCandidate {$peeringcandidate} )."
# retrieve the provider node id and ip
provider_node_id=$(kubectl get peeringCandidates $peeringcandidate -n fluidos -o jsonpath='{.spec.flavor.spec.owner.nodeID}')
provider_ip=$(kubectl get peeringCandidates $peeringcandidate -n fluidos -o jsonpath='{.spec.flavor.spec.owner.ip}')

# retrieve the consumer node id and ip
consumer_node_id=$(kubectl get configmap fluidos-network-manager-identity -n fluidos -o jsonpath='{.data.nodeID}')
consumer_ip=$(kubectl get configmap fluidos-network-manager-identity -n fluidos -o jsonpath='{.data.ip}')

path_reservation="./reservation.yaml"
# Check if it's a file (not a directory)
if [ -f "$path_reservation" ]; then
    echo "[+] Processing $path_reservation"
    yq eval ".spec.peeringCandidate.name = \"$peeringcandidate\"" -i "$path_reservation"
    yq eval ".spec.seller.nodeID = \"$provider_node_id\"" -i "$path_reservation"
    yq eval ".spec.seller.ip = \"$provider_ip\"" -i "$path_reservation"
    yq eval ".spec.buyer.nodeID = \"$consumer_node_id\"" -i "$path_reservation"
    yq eval ".spec.buyer.ip = \"$consumer_ip\"" -i "$path_reservation"
fi

echo "[+] Applying the modified reservation"
kubectl apply -f $path_reservation

echo ""

echo "[+] Check reservation status."
kubectl get reservation -n fluidos 


reservation_name=$(kubectl get reservation -n fluidos | grep sample | awk '{print $1}')
contract_name=$(kubectl get reservation $reservation_name -n fluidos -o jsonpath='{.status.contract.name}')

path_allocation="./allocation.yaml"
if [ -f "$path_reservation" ]; then
    echo "[+] Processing $path_reservation"
    yq eval ".spec.contract.name = \"$contract_name\"" -i "$path_allocation"
fi

echo ""
echo "[+] Applying the allocation."
kubectl apply -f ./allocation.yaml


