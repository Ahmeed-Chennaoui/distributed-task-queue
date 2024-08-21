1) Create monitoring namespace
```bash
kubectl create namespace monitoring
```
2) Change namespace role to be able to monitor 
```bash
kubectl create -f cluster-role.yaml
```
3) Add config map to be able to change Prometheus config on the fly
```bash
kubectl create -f prometheus-configMap.yaml
```
4) Create Prometheus deployment
```bash
kubectl create -f prometheus-deployment.yaml
```