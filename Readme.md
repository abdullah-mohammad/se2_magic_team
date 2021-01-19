# GoShare 

## Usage 

You need to start both the backend and the frontend individually.
For the Backend, you need to `gradle bootRun` in the root repo to start the api on Port 8080.

For the Frontend, you first need to `npm install`(only once) in the the folder `frontend_eborrow` and then `npm run
 serve`. This will
 start the Vue Frontend on Port 8081. 
  
 When both are running, you can open your browser at `localhost:8081` to see the App.
 
 Access to the database - localhost:8080/h2-console
 
 Username:sa 

 Password should be left blank.
 
 JDBK URL: jdbc:h2:mem:eborrow


## References

https://bezkoder.com/spring-boot-vue-js-crud-example/
https://bezkoder.com/spring-boot-jwt-authentication/
https://bezkoder.com/jwt-vue-vuex-authentication/

## Deployen auf dem HAW ICC 
Das Deployment in Kubernetes wird relativ umfangreich, daher teile ich diese Aufgabe in kleinere Teilaufgaben.

### 1. Keep it simple, nur das backend, manuell
Dockerfile mit dem Inhalt:
```dockerfile
FROM gradle:6.7.1-jdk11
COPY settings.gradle build.gradle ./
COPY src ./src/
CMD ["gradle","bootRun"]
```
`docker run --publish 8080:8080 goshare-backend`

Mit: `docker run -it --entrypoint /bin/bash goshare-backend` kann zum debuggen eine shell im container geöffnet werden.

#### Hochladen in das icc

`docker login git.haw-hamburg.de:5005`

`docker build -t goshare-backend .`

Der spezielle Alias muss vergeben werden, dieser findet sich in Gitlab -> Packages -> Container Registry 
in dem Fall ist das: `git.haw-hamburg.de:5005/acq573/se2_magic_team`

Alias vergeben: `docker tag goshare-backend git.haw-hamburg.de:5005/acq573/se2_magic_team:backend`.

Das image ins icc hochladen: `docker push git.haw-hamburg.de:5005/acq573/se2_magic_team:backend`

#### Deployment auf dem icc

Erst braucht kubernetes ein GitLab Deploy Token, damit es auf das private repository zugreifen kann.
GitLab -> Settings -> Repository -> Expand Deploy Token
Dort ein neues Token erstellen das den read_registry Zugriff hat. Der Name des Tokens ist willkürlich `se2deploytoken`. Es gibt den username `gitlab+deploy-token-942` und das token `hXrhX_GaGS9hx35gWEyW` zurück.

Jetzt mit `kubelogin` einloggen und ein secret erstellen, damit kubernetes auf das repo zugreifen darf. Hierbei sind die Daten aus dem vorher generierten Token wichtig.

```
kubectl create secret docker-registry <SecretName> -n <ProjectNamespace> \
--docker-server=git.haw-hamburg.de:5005 \
--docker-username=<token-benutzername> \
--docker-password=<token-passwort> \
--docker-email=<your-email>
```

Den Namespace findet man in GitLab bei Operations -> Kubernetes -> haw-icc (kann bei einem neuen Repository eine Weile dauern bis es dort vorhanden ist) -> Expand provider Details -> Project namespace ganz unten.

```
kubectl create secret docker-registry se2deploysecret -n acq573-se2-magic-team \
--docker-server=git.haw-hamburg.de:5005 \
--docker-username=gitlab+deploy-token-942 \
--docker-password=hXrhX_GaGS9hx35gWEyW \
--docker-email=matthias.mildenberger@haw-hamburg.de
```

Als nächstes muss die `deploy.yml` entsprechend vorbereitet werden, dass sie sich das korrekte image bezieht.
Hier kann man zunächst exakt das gleiche script verwenden wie im Tutorial (das korrekte Image und den Secret key natürlich eintragen.)

Mit `kubectl apply -f deploy.yaml -n acq573-se2-magic-team` kann dann das Backend deployed werden. 

Mit `kubectl get deploy,pod,svc -n acq573-se2-magic-team` kann der Status überprüft werden.

Mit `kubectl delete deployment goshare-backend -n acq573-se2-magic-team` kann der der Pod gelöscht werden.

Um Es wieder zu löschen:
`kubectl delete service goshare-backend -n acq573-se2-magic-team && kubectl delete deployment goshare-backend -n acq573-se2-magic-team`

Mit `kubectl exec -it <Name des Pods> -n acq573-se2-magic-team --bash` kann eine Interaktive shell geöffnet werden. (Name des Pods kann mit dem Befehl get deploy von oben rausgefunden werden z.B. `kubectl exec -it pod/goshare-backend-7cc698cf9f-zxfdk -n acq573-se2-magic-team --bash`).

Jetzt muss der haw proxy aktiviert werden mit `kubectl proxy` und dann kann mit `localhost:8001/api/v1/namespaces/acq573-se2-magic-team/services/goshare-backend:http/proxy/` auf das Projekt zugegriffen werden (z.B. am Ende der Route noch `items` hinzufügen um alle Items anzuzeigen).

//TODO: momentan kann man noch keine Authorized requests ausführen von postman aus. Die Header werden nicht mit übermittelt, vielleicht klappt das ganze wenn man es mit dem vue Frontend ausführt. 

### 2. One Step further, das Frontend, manuell
Die folgenden Befehle und Dateien befinden sich im frontend_eborrow Ordner. Das Dockerfile wurde nach der Vorlage von Vuejs angefertigt. https://cli.vuejs.org/guide/deployment.html#docker-nginx

`cd frontend_eborrow`

`docker build -t goshare-frontend .`

`docker run --publish 80:80 goshare-frontend`

Damit das Docker Frontend auf dem lokalen Rechner läuft muss die API Url in dem `.env.production` auf localhost angepasst werden. Ansonsten steht hier die Adresse welcher der backend Service in kubernetes erhält.

#### Hochladen in das icc

`docker login git.haw-hamburg.de:5005`

`docker build -t goshare-frontend .`

Der spezielle Alias muss vergeben werden, dieser findet sich in Gitlab -> Packages -> Container Registry 
in dem Fall ist das: `git.haw-hamburg.de:5005/acq573/se2_magic_team`. Ich füge am Ende noch das Wort frontend ein damit der container einen anderen Namen hat als das backend.

Alias vergeben: `docker tag goshare-frontend git.haw-hamburg.de:5005/acq573/se2_magic_team:frontend`.

Das image ins icc hochladen: `docker push git.haw-hamburg.de:5005/acq573/se2_magic_team:frontend`

#### Deployment auf dem icc

Das Sicherheitstoken wurde bereits für das Backend erstellt.
Auch für das frontend muss eine entsprechende yaml Datei zum deployen erstellt werden, diese nenne ich `frontend.yaml`.

Mit `kubectl apply -f frontend.yaml -n acq573-se2-magic-team` kann dann das Frontend deployed werden. 

Mit `kubectl get deploy,pod,svc -n acq573-se2-magic-team` kann der Status überprüft werden.

Mit `kubectl delete deployment goshare-frontend -n acq573-se2-magic-team` kann der der Pod gelöscht werden.

Mit `kubectl exec -it <Name des Pods> -n acq573-se2-magic-team --bash` kann eine Interaktive shell geöffnet werden. (Name des Pods kann mit dem Befehl get deploy von oben rausgefunden werden z.B. `kubectl exec -it pod/goshare-frontend-7cc698cf9f-zxfdk -n acq573-se2-magic-team --bash`).


Jetzt muss der haw proxy aktiviert werden mit `kubectl proxy` und dann kann mit `localhost:8001/api/v1/namespaces/acq573-se2-magic-team/services/goshare-frontend:http/proxy/` auf das Projekt zugegriffen werden

Um das Projekt wieder zu entfernen: 
`kubectl delete service goshare-frontend -n acq573-se2-magic-team && kubectl delete deployment goshare-frontend -n acq573-se2-magic-team`

Man kann mit dem Proxy das Backend nicht richtig ansprechen lassen vom frontend, mit port forwarding hingegen funktioniert es. 
Um jetzt das Projekt "richtig" mit dem Backend zu verbinden müssen das Backend und das frontend von außen erreichbar sein, da auf der Seite des Clients die Anfragen an das backend gesendet werden und daher nicht über den localhost in dem Cluster auf die API zugegriffen werden kann. 

`sudo kubectl port-forward -n acq573-se2-magic-team goshare-frontend-57bfbb5b7f-ckbnd 80:80`

`sudo kubectl port-forward -n acq573-se2-magic-team goshare-backend-8f8b5698c-k56gh 8080:8080`

### 3. Go Live on the internet
#### Backend
Um live zu gehen müssen wir Ingress Resourcen erstellen, damit die Anfragen die an die Domain kommen an den entsprechenden Service in kubectl weitgergeleitet werden. Das ingress ist eine yaml Datei die auch mit apply hochgeladen wird.
`kubectl apply -f ingress_backend.yaml -n acq573-se2-magic-team`

Der aktuelle Stand der Ingress Resource kann mit 
`kubectl get ingress -n acq573-se2-magic-team`

#### Frontend
Auch für das Frontend wurde eine entsprechende ingress.yaml erstellt.
Das Frontend muss dafür noch einmal neu gestartet werden bzw. das Dockerfile auch neu gebaut, weil die api adresse sich geändert hat.
`kubectl apply -f ingress_frontend.yaml -n acq573-se2-magic-team`


### 4. Auto Deployment Pipeline in gitlab

Dazu wurde die Pipeline `.gitlab-ci.yaml` geschrieben.

# Quellen

https://userdoc.informatik.haw-hamburg.de/lib/exe/fetch.php?media=docu:icc_tutorial_hello_world.pdf
https://vuejs.org/v2/cookbook/dockerize-vuejs-app.html

