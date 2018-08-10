In this directory

mvn install 

docker build . -t ccri-oauthserver

docker tag ccri-oauthserver thorlogic/ccri-oauthserver:1.0.dev

docker push thorlogic/ccri-oauthserver


docker run -d -p 8182:8182 ccri-tie 

