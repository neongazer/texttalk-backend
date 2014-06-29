class zookeeper {
	
	package { 'zookeeper':
		ensure => "3.3.5*",
	}
	package { 'zookeeperd':
		ensure => "3.3.5*",
		require => Package["zookeeper"],
	}
}