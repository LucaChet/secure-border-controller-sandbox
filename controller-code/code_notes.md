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

## TODO: sync 1
1. ✅ aggiungere `"liqo"` direttamente nella lista dei namespaces da escludere (`namespacesToExclude`), per snellire il controllo sui namespaces.
>   ⚠️ Se il namespace `"liqo"` cambia nome ad esempio con tag casuali, questo approccio è da rivedere
2. ✅ nel watcher `watchNamespaces`, quando un NS viene eliminato sarebbe quantomeno da togliere dalla lista dei NS offloaded, locale al controller, chiamata `offloadedNamespaces`
3. ✅ nel watcher `watchNamespaces`, quando un namespace viene modificato, il controllo è incoerente con le altre due condizioni (namespace `ADDED` e `DELETED`): da implementare check che il namespace sia offloadato, non che non sia tra quelli da escludere
4. ✅ migliorare la leggibilità e la funzionalità della funzione `startModuleTimer`, rimuovere il catch vuoto
5.  sostituire timer di 5s con attesa automatica della risorsa `networkRequests` affinchè quando questa è pronta si esegua l'operazione
6. ✅ rimuovere il thread `applyDefaultNetworkPolicies` spostandone la logica dentro al watcher dei namespace
>   ⚠️ Da testare
7. migliorare la funzione `verify`, rimuovendo il riferimento hardcoded al file XML contenente gli intenti in MSPL.
>   ⚠️ Possibili soluzioni:
>    - Usare un file di configurazione esterno
>    - Usare una variabile d'ambiente
>    - altro..?
8. ✅ testare la funzione `verify` così da avere prova che funzioni, scoprire perchè passa il check sul primo peeringcandidate
9. ✅ la funzione `createProviderCluster` *non ha senso* 
10. per testare il controllore come componente di un cluster KinD: 
    - compilare il codice sorgente
    - creare una nuova docker image usando il Dockerfile
    - cambiare il template del manifesto YAML

## TODO: sync 2
1. PER LA DEMO! la `verify` dovrebbe restituire un ranking dei candidati al peering anzichè semplicemente escludere o accettare (true/false) i singoli candidati. In questo modo l'armonizzazione verrebbe sfruttata appieno nel suo algoritmo complesso, visto che al momento uno dei due lati degli intenti (richiesta o autorizzazione) sono una wildcard `*` che rende banale l'armonizzazione.
2. `verify` non deve avere il cluster tra gli argomenti, ma soltanto i `requestIntent` e gli `authorizationIntent`
3. ✅ rimuovere le mappe dalla verify, visto che tanto non viene effettuato il check a basso livello sui pods ma soltanto ad alto livello sulle label
4.  creare un watcher su una configMap (quella di UMU)
5. ✅ trovare un modo per aspettare che delle risorse siano pronte: quando ho ricevuto i PeeringCandidates devo aspettare la ConfigMap di UMU per poi accedervi una volta che esiste ed è popolata
>   ⚠️ busy waiting con while loop
6. DEMO: lanciare un test con n provider e un solo consumer 
