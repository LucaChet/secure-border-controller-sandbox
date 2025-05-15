#export KUBECONFIG=../demo_da_presentare/node-main/tools/scripts/fluidos-consumer-1-config
echo " [+] In CONSUMER cluster..."
# Populate the consumer cluster with the initial resources
echo " [+] Creating namespaces and deployments."
#kubectl create namespace payments
#kubectl create namespace products
kubectl create namespace users
#kubectl label namespace payments name=payments
#kubectl label namespace products name=products
kubectl label namespace users name=users
kubectl apply -f ./deployments/consumer/local
# Deploy the custom controller
echo "[+] Creating Service Accont for the custom controller"
kubectl create serviceaccount custom-controller -n fluidos
echo "[+] Creating all the needed roles/clusterRoles and bindings"
kubectl apply -f ./serviceAccounts
echo "[+] Deploying the custom controller"
kubectl apply -f ./custom-controller.yaml 