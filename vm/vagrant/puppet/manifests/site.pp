define append_if_no_such_line($file, $line, $refreshonly = 'false') {
   exec { "/bin/echo '$line' >> '$file'":
      unless      => "/bin/grep -Fxqe '$line' '$file'",
      path        => "/bin",
      refreshonly => $refreshonly,
   }
}

class must-have {

  require java7

  exec { "apt-get-update":
  	command	=> "/usr/bin/apt-get update",
  }

  package { "ia32-libs":
      ensure	=> present,
      require	=> Exec["apt-get-update"],
  }

  package { "lame":
    ensure	=> present,
    require	=> Exec["apt-get-update"],
  }

  package { "wget":
    ensure	=> present,
    require	=> Exec["apt-get-update"],
  }

  package { "mc":
    ensure	=> present,
    require	=> Exec["apt-get-update"],
  }

  package { "htop":
    ensure	=> present,
    require	=> Exec["apt-get-update"],
  }

  host { 'storm':
    ip => '127.0.0.1',
  }

  host { 'nimbus':
    ip => '127.0.0.1',
  }

  host { 'zookeeper':
    ip => '127.0.0.1',
  }

  host { 'redis':
    ip => '127.0.0.1',
  }
}

class hiera-classes {
  require must-have
  hiera_include('classes')
}

class install-all {

  require hiera-classes
  require redis
}

include install-all
