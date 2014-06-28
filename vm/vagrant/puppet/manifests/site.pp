define append_if_no_such_line($file, $line, $refreshonly = 'false') {
   exec { "/bin/echo '$line' >> '$file'":
      unless      => "/bin/grep -Fxqe '$line' '$file'",
      path        => "/bin",
      refreshonly => $refreshonly,
   }
}

class must-have {

  exec { "apt-get":
  	command	=> "/usr/bin/apt-get update",
  }

  package { "ia32-libs":
      ensure	=> present,
      require	=> Exec["apt-get"],
  }
}

include must-have
hiera_include('classes')
