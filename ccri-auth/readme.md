In this directory

mvn install 

docker build . -t ccri-oauthserver

docker tag ccri-oauthserver thorlogic/ccri-oauthserver:1.0.dev

docker push thorlogic/ccri-oauthserver


docker run -d -p 20080:8080 ccri-auth 

