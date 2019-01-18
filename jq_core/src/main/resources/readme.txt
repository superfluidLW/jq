可以通过如下命令, 将需要用到的 Cfets 自定义包安装到本地 Maven 仓库(依赖的包已经通过pom导入)

mvn install:install-file -Dfile="taxchina_common.jar" -DgroupId=com.taxchina.common -DartifactId=taxchina_common -Dversion=1.0-SNAPSHOT -DcreateChecksum=true -Dpackaging=jar -DgeneratePom=true

mvn install:install-file -Dfile=D:/TaxChina/git/taxchina/taxchina_common/target/taxchina_common-1.0-SNAPSHOT.jar -DgroupId=com.taxchina.common -DartifactId=taxchina_common -Dversion=1.0 -Dpackaging=jar