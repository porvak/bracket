
# Create a pair of lookups and creations for each define:

create_resources('tomcat::instance', hiera('tomcat_instances', []))

