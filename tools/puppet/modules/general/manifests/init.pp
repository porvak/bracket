# Installs the general components necessary for all servers
class general ( $ensure = 'latest' ){

    $packages=[
        'wget',
        'ftp',
        'unzip',
        'zip',
        'git',
        'vim-enhanced',
        'screen',
        'tree',
        'net-snmp',
        'ntp',
        'curl',
        'jpackage-utils'
    ]

    ensure_packages($packages)

}
