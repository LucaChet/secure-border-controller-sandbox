# Controller Code Review Notes
## KubernetesController class
Si potrebbe quasi rinominare per coerenza con il nome del progetto, essendo il cuore della logica. Proposte:
- `KubernetesSecureBorderController`
- `KSBC`
- `KubernetesSBC`

### Funzionamento
- Creato dal main all'avvio della logica, crea un client per il K8s API server ed effettua l'autenticazione con il token associato al service account.
- Avviato dal main con il metodo `start()`, il quale crea i watcher sulle risorse mediante dei thread dedicati (`podThread`,`namespaceThread`, `tunnelEndpoointThread`, `defaultNetworkPolicyThread` e `peeringCandidatesThread`)
- I watcher si occupano di monitorare le risorse reagendo ad eventi di creazione, eliminazione o eventualmente offloading:
    
    **1.** watchPods: monitora i pod e crea una network policy per ogni pod che viene creato su un namespace remoto, per metterlo in comunicazione con il namespace locale.
    >   ⚠️ Se il pod viene eliminato, non si occupa di eliminare la policy

    **2.** applyDefaultDenyNetworkPolicies: monitora i namespace e quando rileva un nuovo namespace, se NON è nella lista dei namespace esclusi (o "liqo"), crea una default `DENY` policy e una `ALLOW` per il KubeDNS. Inoltre, se il namespace è remoto (offloaded) crea una policy per metterlo in comunicazione con il namespace locale.
                
    >   ⚠️ Se il namespace viene eliminato, non si occupa di eliminare la policy

    **3.** watchNamespaces: monitora i namespace, quando rileva la creazione di un nuovo namespace offloaded, lo aggiunge alla lista dei namespace offloaded del controllore. Inoltre, sempre per i namespace offloaded fa una chiamata alla funzione `startModuleTimer` che avvia il processo di armonizzazione degli intenti e la creazione delle network policies sulla base di questi ultimi.
                
    >   ⚠️ Se il namespace offloaded viene eliminato, non esegue nessuna azione

    **4.** watchTunnelEndpoint: monitora la risorsa `TunnelEndpoint` e quando ne viene creata una nuova inserisce la coppia `clusterID` e lista di IP consentiti nella lista degli ip consentiti locale al controllore. 

    **5.** watchPeeringCandidates: monitora i peering candidates ricevuti automaticamente, quando ne rileva uno nuovo, ne estrae gli `authorizationIntents` e chiama il verifier per 

### classe Harmonization Controller 
- Classe wrapper per `HarmonizationSerivice` con solo due metodi di `verify` e `harmonize`.

### classe Harmonization Service
- contiene un oggetto `HarmonizationData`, un `ClusterService` e due parametri **hardcoded** che puntano a files contenenti gli intenti di provider e consumer.
- i metodi di questa classe sono utili a:
    1. estrarre gli intenti dal provider e dal consumer direttamente dai files (metodo `harmonize` e metodo `verify`) e fare l'effettiva armonizzazione/vreifica

## Dubbi & Considerazioni
- le `System.out.println` stampano a console, ma dove vanno a finire quando il codice è in esecuzione in un container dentro il cluster? Idem per i log
- come si testano eventuali modifiche al codice?
- non vedo l'armonizzazione delle discordanze di Tipo 3 nella funzione `harmonize` del `HarmonizationService`, ma solo quelle di tipo 1 e 2. Perché?
- tutte le classi definite in gen-src? Sono generate automaticamente da JAXB?
- HarmonizationUtils.java è fatto tutto da Chat: per coerenza col resto lo renderei più "human written"
- Perchè la classe `HarmonizationService` sembra essere specializzata per la demo? (definisce il metodo `createProviderCluster` e fa uso dei files hardcoded)?