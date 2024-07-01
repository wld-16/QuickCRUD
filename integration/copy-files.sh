#!/bin/bash
echo "Script executed from: ${PWD}"
cp -rf src/test/kotlin/wld/accelerate/quickcrud/java/controller/ integration/spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/java/entity/ integration/spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/java/model/ integration/spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/java/service/ integration/spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/java/repository/ integration/spring-boot-server/src/main/java/wld/accelerate/quickcrud/java/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/sql/ integration/spring-boot-server/src/main/resources/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/vue/plugins/ integration/vue3-client/src/
cp -rf src/test/kotlin/wld/accelerate/quickcrud/vue/components/ integration/vue3-client/src/