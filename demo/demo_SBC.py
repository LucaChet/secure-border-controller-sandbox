import os
import time
import subprocess
from tkinter import Tk, Label, PhotoImage

# Utility function to wait for user confirmation
def wait_for_user(prompt="Press Enter to continue..."):
    input(prompt)

# Function to open an image in a separate window using Tkinter
def show_image_window(title, image_path):
    window = Tk()
    window.title(title)
    img = PhotoImage(file=image_path)
    label = Label(window, image=img)
    label.pack()
    # Keep the window open without blocking the rest of the script
    window.after(100, lambda: None)  # Ensures the window updates
    window.mainloop()

# Function to open a new terminal window and run a command
def open_terminal_with_command(command, title="Terminal"):
    # For Linux (using gnome-terminal or x-terminal-emulator)
    subprocess.Popen([
        'gnome-terminal',
        '--title=' + title,
        '--', 'bash', '-c', f'{command}; exec bash'
    ])

    # Uncomment below if you're on macOS using Terminal
    # os.system(f'''osascript -e 'tell application "Terminal" to do script "{command}"' ''')

    # Uncomment below if you're on Windows using cmd
    # subprocess.Popen(["start", "cmd", "/k", command], shell=True)

# === DEMO SCRIPT STARTS HERE ===

print("Starting automated demo...")

# Step 1: Initial prompt
wait_for_user("Step 1: Preparing the environment. Press Enter when ready...")

# Step 2: Launch Kubernetes clusters (simulation)
open_terminal_with_command("echo 'Starting Kubernetes Cluster A...'; sleep 2", title="K8s Cluster A")
open_terminal_with_command("echo 'Starting Kubernetes Cluster B...'; sleep 2", title="K8s Cluster B")

wait_for_user("Step 2: Clusters are starting. Press Enter to continue...")

# Step 3: Show image in a new window
show_image_window("Demo Diagram", "demo_cover.png")

# You can keep adding more steps below
wait_for_user("Step 3: Image shown. Press Enter to proceed with the next steps...")

print("Demo automation completed.")
