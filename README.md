#TODO LIST

1. Upload data to virtuoso database
    `
     1. isql *OR* isql 1111 dba dba *OR* isql localhost dba dba 
     1. SELECT ll_graph, ll_file FROM DB.DBA.LOAD_LIST;
     1. ld_dir ('/path/to/files', '*.ttl.gz', 'http://dbpedia.org');
     1. rdf_loader_run();
    `
1. Static IP on atleast 3 machines
    '
    My IP description
    IPv-4

    DEVICE="eth0"
    NM_CONTROLLED="yes"
    ONBOOT=yes
    HWADDR=20:89:84:c8:12:8a
    TYPE=Ethernet
    BOOTPROTO=static
    NAME="System eth0"
    UUID=5fb06bd0-0bb0-7ffb-45f1-d6edd65f3e03
    IPADDR= 2001:db8::c0ca:1eaf
    NETMASK=255.255.255.0
    '
    2. https://gist.github.com/fernandoaleman/2172388
    
    3.
        # vi /etc/sysconfig/network-scripts/ifcfg-eth0
        DEVICE=eth0
        HWADDR=1c:65:9d:93:fc:e1
        TYPE=Ethernet
        ONBOOT=yes
        BOOTPROTO=none
        IPV6INIT=no
        USERCTL=no
        NM_CONTROLLED=yes
        PEERDNS=yes
        IPADDR=192.168.0.101
        NETMASK=255.255.255.0
    Now,   configure default getaway:

        # vi /etc/sysconfig/network
        NETWORKING=yes
        HOSTNAME=centos6
        GATEWAY=192.168.0.1 
1. Get hadoop working  
        1. Uninstall old java  
        2. Put new java  
        3.*poof*
1. Unzip and split the Wikipedia file. 5-7 GB splits.  _LOTS OF TIME_
    `split -b 1024m "file.tar.gz" "file.tar.gz.part-"`
    combine `copy /b file1 + file2 + file3 + file4 filetogether`

#Sub TODO

1. Inverted Index with sentences.
    Looks like done...needs _longer sentences to be returned_

#Later

1. Pick EPO from infoboxes
1. Query it in the raw wikidata
1. Match it in the category graph 
       1. Create path from `E category` to `O Category` add weights as and when it is traversed.