# space-invaders-detector

A handy tool to track down space invaders.

The application reads a radar sample and reveals possible locations of aliens.

## Usage

### Build

```shell
lein uberjar
```

### Run

The input params for application are two text files:
* **--data**        file with radar image
* **--patterns**    file with known pictures of space invaders
* **--threshold**   (optional) number [0-100] for setting detection accuracy (default 75)

```shell
java -jar target/detector.jar --data resources/radar-sample.txt --patterns resources/invader-samples.txt

java -jar target/detector.jar --data resources/radar-sample.txt --patterns resources/invader-samples.txt --threshold 85
```
