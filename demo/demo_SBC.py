import os
import signal
import time
import subprocess
import sys
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
    input(prompt)

def show_image_window_async(title, image_path, width=400, height=300, x=100, y=100):
    win = {}

    def create_window():
        window = Tk()
        window.title(title)
        window.geometry(f"{width}x{height}+{x}+{y}")
        window.protocol("WM_DELETE_WINDOW", lambda: None)  # Disable manual close button

        img = PhotoImage(file=image_path)
        label = Label(window, image=img)
        label.image = img
        label.pack(expand=True)

        win["window"] = window
        window.mainloop()

    t = threading.Thread(target=create_window)
    t.daemon = True
    t.start()

    return win

# Function to open a new terminal window and run a command
def open_terminal_with_command(command, title="Terminal"):
    return subprocess.Popen([
        'konsole',
        '--new-tab',  
        #'--hold',      mantiene il terminale aperto dopo l'esecuzione
        '-p', f'tabtitle={title}',
        '-e', 'bash', '-c', f'{command}; exec bash'
    ])


def open_cluster_terminal(kubeconfig_path, title):
    command = f'export KUBECONFIG={kubeconfig_path};'
    open_terminal_with_command(command, title=title)

# Later: kill the terminal process
def close_terminal(proc):
    try:
        os.kill(proc.pid, signal.SIGTERM)
        print(f"[+] Terminal (PID {proc.pid}) closed.")
    except Exception as e:
        print(f"[!] Error closing terminal: {e}")

# === DEMO SCRIPT STARTS HERE ===

print("Starting automated demo")

#wait_for_user("Step 1: Checking that all needed software is installed. Press Enter when ready to begin...")
#TODO check if all needed software is installed and eventually exit

#wait_for_user("Step 2: Open two terminal windows to interact with the two K8s Clusters. Press Enter to continue...")
#consumer_watch = open_terminal_with_command("export KUBECONFIG=./mock/config; watch kubectl get pods -n fluidos", "Consumer Cluster") #TODO real kubeconfig 
#provider_watch = open_terminal_with_command("export KUBECONFIG=./mock/config; watch kubectl get pods -n fluidos", "Consumer Cluster")

wait_for_user("Step 3: Configure the first Kubernetes cluster, the Consumer. Press Enter to begin...")
# First the consumer cluster
mock_steps = """
sleep 2
"""
subprocess.run("export KUBECONFIG=./mock/config;", shell=True, check=True)
steps_cons =  """
    kubectl create namespace users
    kubectl label namespace users name=users
    kubectl apply -f ./deployments/consumer/local
    sleep 3
    kubectl create serviceaccount secure-border-controller -n fluidos
    kubectl apply -f ./serviceAccounts
    kubectl apply -f ./secure-border-controller.yaml"""
run_command_with_dot_circle(mock_steps) #TODO steps_cons
print("[+] Consumer cluster configured:")
print("\t[+] Created namespaces.")
print("\t[+] Created serviceaccounts.")
print("\t[+] Started local worloads.")
print("\t[+] Deployed the Secure Border Controller.")

wait_for_user("Step 4.1: Configure the second Kubernetes cluster, the Provider. Press Enter to begin...")
# Then the provider
subprocess.run("export KUBECONFIG=./mock/config;", shell=True, check=True) #TODO point to provider
steps_prov = """
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
run_command_with_dot_circle(mock_steps) #TODO steps_prov
wait_for_user("Step 4.2: create and expose the Flavors to advertise resources available on the Provider cluster. Press Enter to continue...")

steps_prov ="""
provider_node_id=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.nodeID}')
provider_ip=$(kubectl get flavors -n fluidos -o jsonpath='{.items[0].spec.owner.ip}')
kubectl delete flavors --all -n fluidos

for FILE in flavors/*; do
    if [ -f "$FILE" ]; then
        yq eval ".spec.owner.nodeID = \"$provider_node_id\"" -i "$FILE"
        yq eval ".spec.owner.ip = \"$provider_ip\"" -i "$FILE"
        yq eval ".spec.providerID = \"$provider_node_id\"" -i "$FILE"  
    fi
done

kubectl apply -f flavors
"""
run_command_with_dot_circle(mock_steps) #TODO steps_prov
print("[+] Provider cluster configured:")
print("\t[+] Created namespaces.")
print("\t[+] Created serviceaccounts.")
print("\t[+] Started local worloads.")
print("\t[+] Deployed the Secure Border Controller.")
print("\t[+] Deployed Flavor CRDs to advertise resources available for purchase.")


#close_terminal(consumer_watch)
#close_terminal(provider_watch)

print("Step 5: Configuration completed. The following image shows the two clusters and the resources running in them.")
win_ref = show_image_window_async("Demo Diagram", "demo_cover.png")
wait_for_user("Press Enter to close the image window and proceed...")

if "window" in win_ref:
    win_ref["window"].destroy()

wait_for_user("Step 6: Image shown. Press Enter to proceed with the next steps...")

print("Demo automation completed.")
