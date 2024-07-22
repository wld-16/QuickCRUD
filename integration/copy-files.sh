#!/bin/bash
echo "Script executed from: ${PWD}"
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/java/controller/ spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/java/entity/ spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/java/model/ spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/java/service/ spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/java/repository/ spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/sql/ spring-boot-server/src/main/resources/db/
cp ../src/test/kotlin/wld/accelerate/quickcrud/sh/executeSQL.sh spring-boot-server/src/main/resources/db/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/vue/plugins/ vue3-client/src/
cp -rf ../src/test/kotlin/wld/accelerate/quickcrud/vue/components/ vue3-client/src/
echo "Has Copied Files"