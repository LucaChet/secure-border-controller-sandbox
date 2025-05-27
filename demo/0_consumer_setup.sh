echo " [+] In CONSUMER cluster..."
echo " [+] Creating namespaces and deployments."
kubectl create namespace users
kubectl label namespace users name=users
kubectl apply -f ./deployments/consumer/local
# Deploy the custom controller
echo "[+] Creating Service Accont for the secure border controller"
kubectl create serviceaccount secure-border-controller -n fluidos
echo "[+] Creating all the needed roles/clusterRoles and bindings"
kubectl apply -f ./serviceAccounts
echo "[+] Deploying the secure border controller"
kubectl apply -f ./secure-border-controller.yaml 