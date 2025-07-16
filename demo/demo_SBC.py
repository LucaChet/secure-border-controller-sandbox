import os
import signal
import time
import subprocess
import shutil
import sys
from multiprocessing import Process
import threading
from tkinter import Tk, Label, PhotoImage

def run_command_with_rotating_circle(command):
    # Posizioni del cerchio, rappresentate con punti in un "cerchio"
    # Qui usiamo 8 posizioni come punti disposti circolarmente
    circle_frames = [
        "●       ",
        " ●      ",
        "  ●     ",
        "   ●    ",
        "    ●   ",
        "     ●  ",
        "      ● ",
        "       ●",
    ]

    done = False

    def run_command():
        nonlocal done
        subprocess.run(command, shell=True)
        done = True

    thread = threading.Thread(target=run_command)
    thread.start()

    i = 0
    while not done:
        frame = circle_frames[i % len(circle_frames)]
        sys.stdout.write(f"\r{frame} Loading...")
        sys.stdout.flush()
        time.sleep(0.15)
        i += 1

    sys.stdout.write("\rDone!        \n")
    sys.stdout.flush()

def run_command_with_dot_circle(command):
    circle_positions = ['●', '.', '.', '.', '.', '.', '.', '.']
    done = False

    def run_command():
        nonlocal done
        subprocess.run(command, shell=True)
        done = True

    thread = threading.Thread(target=run_command)
    thread.start()

    i = 0
    while not done:
        circle = ['.' for _ in range(8)]
        circle[i % 8] = '●'
        sys.stdout.write('\r' + ' '.join(circle) + ' Running...')
        sys.stdout.flush()
        time.sleep(0.2)
        i += 1

    sys.stdout.write('\r' + '● ● ● ● ● ● ● ● Done!      \n')
    sys.stdout.flush()
# Utility function to wait for user confirmation
def wait_for_user(prompt="Press Enter to continue..."):
    colored_prompt = f"\033[94m{prompt}\033[0m"  # Blue print
    input(colored_prompt)



def show_image_window_process(title, image_path, width=1200, height=500, x=700, y=900):
    def window_process():
        window = Tk()
        window.title(title)
        window.geometry(f"{width}x{height}+{x}+{y}")
        window.protocol("WM_DELETE_WINDOW", lambda: None)

        img = PhotoImage(file=image_path)
        label = Label(window, image=img)
        label.image = img  # Previene garbage collection
        label.pack(expand=True)

        window.mainloop()

    # Avvia la finestra in un processo separato
    p = Process(target=window_process)
    p.start()
    return p

# Function to open a new terminal window and run a command
def open_terminal_with_command(command, title="Terminal"):
    return subprocess.Popen([
        'konsole',
        '--new-tab',  
        #'--hold',      mantiene il terminale aperto dopo l'esecuzione
        '-p', f'tabtitle={title}',
        '-e', 'bash', '-c', f'{command}; exec bash'
    ])



# Later: kill the terminal process
def close_terminal(proc):
    try:
        os.kill(proc.pid, signal.SIGTERM)
        print(f"[+] Terminal (PID {proc.pid}) closed.")
    except Exception as e:
        print(f"[!] Error closing terminal: {e}")

def check_yq_installed():
    if shutil.which("yq") is None:
        print("\033[91m[ERROR]\033[0m yq is not installed. Please run command 'sudo apt install yq -y' to install it.") #red print
        sys.exit(1)

def exec_shell_by_label(namespace, label, container_name, kubeconfig):
    get_pods_cmd = "kubectl get pods -n {namespace} -l {label} -o jsonpath='{.items[0].metadata.name}' --kubeconfig {kubeconfig}".replace("{namespace}", namespace).replace("{label}", label).replace("{kubeconfig}", kubeconfig)
    result = subprocess.run(get_pods_cmd, capture_output=True, shell=True, text=True)
    if result.returncode != 0 or not result.stdout.strip():
        print(f"\033[91m[ERROR]\033[0m Result is: {result}")
        return
    pod_name = result.stdout.strip()
    exec_cmd = f" kubectl exec -it {pod_name} -n {namespace} -c {container_name} --kubeconfig {kubeconfig} -- sh"
    return open_terminal_with_command(exec_cmd, f"Shell {pod_name}")

def get_pod_ip_by_label(namespace, label, kubeconfig):
    get_pod_ip_cmd = "kubectl get pods -n {namespace} -l {label} -o jsonpath='{.items[0].status.podIP}' --kubeconfig {kubeconfig}".replace("{namespace}", namespace).replace("{label}", label).replace("{kubeconfig}", kubeconfig)
    result = subprocess.run(get_pod_ip_cmd, capture_output=True, shell=True, text=True)
    if result.returncode != 0 or not result.stdout.strip():
        print(f"\033[91m[ERROR]\033[0m Result is: {result}")
        return
    return result.stdout.strip()

# ====== DEMO CONFIGURATION =========

consumer_kubeconfig = "/home/luca/FluidosProject/try/node/tools/scripts/fluidos-consumer-1-config"  # Path to the consumer cluster kubeconfig
provider_kubeconfig = "/home/luca/FluidosProject/try/node/tools/scripts/fluidos-provider-1-config"  # Path to the provider cluster kubeconfig
path_to_xml = "/home/luca/FluidosProject/secure-border-controller-sandbox/orchestrator-code/orchestrator/xml"  # Path to the XML file with consumer request intents
selected_candidate = "peeringcandidate-fluidos.eu-k8slice-demo-3"  # The name of the selected peering candidate for the demo
# ====== DEMO SCRIPT STARTS HERE =========
print("Starting automated demo")

wait_for_user("Step 1: Executing preliminary steps. Press Enter when ready to begin...")
check_yq_installed()
#TODO other checks needed?

wait_for_user("\nStep 2: Open two terminal windows to interact with the two K8s Clusters. Press Enter to continue...")
consumer_watch = open_terminal_with_command(f'export KUBECONFIG={consumer_kubeconfig}; watch kubectl get pods -A', "Consumer Cluster")
provider_watch = open_terminal_with_command(f'export KUBECONFIG={provider_kubeconfig}; watch kubectl get pods -A', "Provider Cluster")

wait_for_user("\nStep 3: Configure the first Kubernetes cluster, the Consumer. Press Enter to begin...")
# First the consumer cluster
mock_steps = """
sleep 2
"""

steps_cons =  f"""
    export KUBECONFIG={consumer_kubeconfig} 
    kubectl port-forward service/orchestrator-service 8080:80 -n fluidos > /dev/null &
    kubectl create namespace users
    kubectl label namespace users name=users
    kubectl apply -f ./deployments/consumer/local
    kubectl create serviceaccount secure-border-controller -n fluidos
    kubectl apply -f ./serviceAccounts
    kubectl apply -f ./secure-border-controller.yaml
    kubectl apply -f ./secure-border-orchestrator.yaml"""
#subprocess.run(steps_cons, shell=True) 
print("[+] Consumer cluster configured:")
print("\t[+] Created namespaces.")
print("\t[+] Created serviceaccounts.")
print("\t[+] Started local worloads.")
print("\t[+] Deployed the distributed Secure Border Controller and Orchestrator.")

wait_for_user("\nStep 4.1: Configure the second Kubernetes cluster, the Provider. Press Enter to begin...")
# Then the provider

steps_prov = f"""
export KUBECONFIG={provider_kubeconfig} 
kubectl create namespace monitoring
kubectl create namespace handle-payments
kubectl label namespace monitoring name=monitoring
kubectl label namespace handle-payments name=handle-payments
kubectl apply -f ./deployments/provider
sleep 2

kubectl create serviceaccount secure-border-controller -n fluidos
kubectl apply -f ./serviceAccounts
kubectl apply -f ./secure-border-controller.yaml
"""

#subprocess.run(steps_prov, shell=True) 
wait_for_user("\nStep 4.2: create and expose the Flavors to advertise resources available on the Provider cluster. Press Enter to continue...")

steps_prov ="""
export KUBECONFIG={provider_kubeconfig}

provider_node_id=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.nodeID}')
provider_ip=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.ip}')
echo "${INFO}   - Provider Node ID: $provider_node_id"
echo "${INFO}   - Provider IP: $provider_ip"
kubectl delete flavors --all -n fluidos
for FILE in flavors/*; do
    if [ -f "$FILE" ]; then
        yq eval ".spec.owner.nodeID = \\"$provider_node_id\\"" -i "$FILE"
        yq eval ".spec.owner.ip = \\"$provider_ip\\"" -i "$FILE"
        yq eval ".spec.providerID = \\"$provider_node_id\\"" -i "$FILE"  
    fi
done
kubectl apply -f flavors
kubectl apply -f ./secure-border-orchestrator.yaml""".replace("{provider_kubeconfig}", provider_kubeconfig)
#subprocess.run(steps_prov, shell=True, executable="/bin/bash", check=True)
print("[+] Provider cluster configured:")
print("\t[+] Created namespaces.")
print("\t[+] Created serviceaccounts.")
print("\t[+] Started local worloads.")
print("\t[+] Deployed the distributed Secure Border Controller and Orchestrator.")
print("\t[+] Deployed Flavor CRDs to advertise resources available for purchase.")


close_terminal(consumer_watch)
close_terminal(provider_watch)

print("\nStep 5: Configuration completed. The following image shows the two clusters and the resources running in them.")
process = show_image_window_process("Demo Diagram", "~/FluidosProject/secure-border-controller-sandbox/demo/demo_cover.png") #TODO real image
wait_for_user("Press Enter to close the image window and proceed...")
process.terminate()
process.join()

wait_for_user("\nStep 6: On the Consumer cluster, monitor the SBC logs to follow its actions. Press Enter to Continue...")
sbc_logs = open_terminal_with_command(f'export KUBECONFIG={consumer_kubeconfig};kubectl logs -f -n fluidos -l app=secure-border-controller', "Consumer SBC Logs")

wait_for_user("\nStep 7: Apply the Solver CRD on the consumer cluster. Press Enter to Continue...")
#subprocess.run(f'export KUBECONFIG={consumer_kubeconfig}; kubectl apply -f solver-custom.yaml', shell=True, check=True) 

wait_for_user("\nStep 8: Note that the solver triggered a discovery process which resulted in the collection of some Peering Candidates...")
#subprocess.run(f'export KUBECONFIG={consumer_kubeconfig}; kubectl get peeringcandidates -n fluidos', shell=True, check=True)

wait_for_user("\nStep 9: The SBC is now waiting for the consumer request intents to be available in a configMap. This is created by the Orchestrator when it receives the intents as an XML file in a HTTP Post...")
#subprocess.run(f'curl -X POST http://localhost:8080/intents/upload -F "file=@{path_to_xml}/consumer-request-intents.xml"', shell="True", executable="/bin/bash")

wait_for_user("\nStep 10: The Verification process selected the first compatible Peering Candidate. Press Enter to proceed with contract creation and resource acquisition...")

steps_cons = """
export KUBECONFIG={consumer_kubeconfig}
provider_node_id=$(kubectl get peeringCandidates {selected_candidate} -n fluidos -o jsonpath='{.spec.flavor.spec.owner.nodeID}')
provider_ip=$(kubectl get peeringCandidates {selected_candidate} -n fluidos -o jsonpath='{.spec.flavor.spec.owner.ip}')
consumer_node_id=$(kubectl get configmap fluidos-node-identity -n fluidos -o jsonpath='{.data.nodeID}')
consumer_ip=$(kubectl get configmap fluidos-node-identity -n fluidos -o jsonpath='{.data.ip}')
path_reservation="./reservation.yaml"
if [ -f "$path_reservation" ]; then
    yq eval ".spec.peeringCandidate.name = \\"{selected_candidate}\\"" -i "$path_reservation"
    yq eval ".spec.seller.nodeID = \\"$provider_node_id\\"" -i "$path_reservation"
    yq eval ".spec.seller.ip = \\"$provider_ip\\"" -i "$path_reservation"
    yq eval ".spec.buyer.nodeID = \\"$consumer_node_id\\"" -i "$path_reservation"
    yq eval ".spec.buyer.ip = \\"$consumer_ip\\"" -i "$path_reservation"
fi
kubectl apply -f $path_reservation
kubectl get reservation -n fluidos
reservation_name=$(kubectl get reservation -n fluidos | grep sample | awk '{print $1}')
until kubectl get reservation reservation-sample -n fluidos -o jsonpath='{.status.contract.name}' 2>/dev/null | grep -q .; do
  echo "Waiting for contract to be created..."
  sleep 5
done
kubectl get reservation -n fluidos 
contract_name=$(kubectl get reservation $reservation_name -n fluidos -o jsonpath='{.status.contract.name}')
path_allocation="./allocation.yaml"
if [ -f "$path_reservation" ]; then
    echo "[+] Processing $path_reservation"
    yq eval ".spec.contract.name = \\"$contract_name\\"" -i "$path_allocation"
fi
kubectl apply -f ./allocation.yaml
sleep 2
""".replace("{consumer_kubeconfig}", consumer_kubeconfig).replace("{selected_candidate}", selected_candidate)
#subprocess.run(steps_cons, shell=True, executable="/bin/bash", check=True)
close_terminal(sbc_logs)

wait_for_user("\nStep 11: The contract is now created and the resources are acquired. The following image shows the two clusters and how they peered to make use of resources continuum...")
process = show_image_window_process("Offloading", "~/FluidosProject/secure-border-controller-sandbox/demo/demo_cover.png") #TODO real image
wait_for_user("Press Enter to close the image window and proceed...")
process.terminate()
process.join()

wait_for_user("\nStep 12.1: Wait for Liqo to fully establish the peering. Press Enter to inspect the peering status on the two clusters...")
consumer_liqo = open_terminal_with_command(f'export KUBECONFIG={consumer_kubeconfig}; watch liqoctl info', "Consumer Cluster Peering Status") 
provider_liqo = open_terminal_with_command(f'export KUBECONFIG={provider_kubeconfig}; watch liqoctl info', "Provider Cluster Peering Status")
wait_for_user("\nStep 12.2: Once the peering reaches the Healty state, you can press Enter to continue...")
close_terminal(consumer_liqo)
close_terminal(provider_liqo)

wait_for_user("\nStep 13: The SBC is now ready to offload some workloads. Press Enter to create new namespaces and offload them...")
consumer = open_terminal_with_command(f'export KUBECONFIG={consumer_kubeconfig}; watch kubectl get pods -A', "Consumer Cluster Offloading")
provider = open_terminal_with_command(f'export KUBECONFIG={provider_kubeconfig}; watch kubectl get pods -A', "Provider Cluster Offloading")

steps_cons = f"""
export KUBECONFIG={consumer_kubeconfig}
kubectl create namespace payments
kubectl create namespace products
kubectl label namespace payments name=payments
kubectl label namespace products name=products
liqoctl offload namespace payments --pod-offloading-strategy Remote
liqoctl offload namespace products --pod-offloading-strategy Remote
kubectl apply -f ./deployments/consumer/offload
sleep 3
"""
#subprocess.run(steps_cons, shell=True, executable="/bin/bash", check=True) 

wait_for_user("\nStep 14: The SBC on the Provider cluster harmonized eventual discordancies between Authorization and Request Intents. It then translated such intents into a set of Kubernetes network policies to automatically enforce the security policies")
close_terminal(consumer)
close_terminal(provider)

wait_for_user("\nStep 15: Check the effectiveness of the network policies enforced by the SBC by testing the connectivity between pods in different namespaces.")
term1 = exec_shell_by_label("monitoring", "app=resource-monitor", "busybox", provider_kubeconfig)
term2 = exec_shell_by_label("payments", "app=app-payment-1", "busybox", consumer_kubeconfig)

wait_for_user("Test 1: Listen for TCP connections on port 80 in the resource-monitor pod. Execute the following command in the resource-monitor pod shell: \n\t \033[0mnc -l -p 80\033[0m")
wait_for_user("Test 1: In the app-payment-1 pod shell, execute the following command to send a message to the resource-monitor pod: \n\t \033[0mecho 'Hello from app-payment-1' | nc "+ get_pod_ip_by_label("monitoring", "app=resource-monitor", provider_kubeconfig)+" 80 -v -w 5\033[0m")

wait_for_user("When you are done testing, press Enter to close the terminal windows and finish the demo.")
close_terminal(term1)
close_terminal(term2)
print("Demo automation completed.")
