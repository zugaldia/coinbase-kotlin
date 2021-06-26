PATH_JAR=./cli/build/libs/cli-0.5.0-SNAPSHOT-all.jar

JAVA_PUBLIC_COMMAND=java -jar $(PATH_JAR)

JAVA_PRIVATE_COMMAND=java -jar $(PATH_JAR) \
	-apikey $(COINBASE_API_KEY) \
	-passphrase '$(COINBASE_API_PASSPHRASE)' \
	-secret $(COINBASE_API_SECRET) \
	-endpoint $(COINBASE_API_ENDPOINT) \

SHARED_HEADERS=-i \
	-H "CB-ACCESS-KEY: $(COINBASE_API_KEY)" \
	-H "CB-ACCESS-PASSPHRASE: $(COINBASE_API_PASSPHRASE)"

ENDPOINT=https://api.pro.coinbase.com

update-docs:
	rm -rf docs/
	./gradlew :lib:dokkaGfm
	mv lib/build/dokka/gfm/lib docs

build-sample:
	./gradlew clean :cli:shadowJar

sample-signature: build-sample
	$(JAVA_PRIVATE_COMMAND) -sample_signature -method GET -path /accounts

sample-private: build-sample
	$(JAVA_PRIVATE_COMMAND) -sample_private

sample-public: build-sample
	$(JAVA_PUBLIC_COMMAND) -sample_public

sample-websocket: build-sample
	$(JAVA_PUBLIC_COMMAND) -sample_websocket

sample-authenticated-websocket: build-sample
	$(JAVA_PRIVATE_COMMAND) -sample_authenticated_websocket

sample-curl: build-sample
	$(eval RESULT=$(shell $(JAVA_PRIVATE_COMMAND) -method GET -path /accounts))
	curl $(SHARED_HEADERS) -X GET $(ENDPOINT)/accounts \
		-H "CB-ACCESS-TIMESTAMP: `echo '$(RESULT)' | jq -r .timestamp`" \
		-H "CB-ACCESS-SIGN: `echo '$(RESULT)' | jq -r .signature`"
