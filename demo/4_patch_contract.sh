##################################################
# MANUAL step: update of contract with configMap #
##################################################

# Before offloading the namespaces, the contract must be updated on the provider's cluster
echo "[+] MANUAL step: update of the Contract injecting name of config map (for Consumer's request intents)."
contract_name=$(kubectl get contracts -n fluidos -o jsonpath='{.items[0].metadata.name}')
request_intent_map="network-intents-example"
#...need to update all the authorization intents + name of the config map
kubectl patch contract -n fluidos $contract_name --type='merge' -p '{"spec": {"networkRequests": "network-intents-example"}}'

##########################
#   end of MANUAL step   #
##########################
