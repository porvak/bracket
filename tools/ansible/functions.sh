#!/bin/bash


# --------------------------------------------------------- # 
# installWget()
# Installs wget if it does not already exist.
# --------------------------------------------------------- # 
function installWget() {
	if ! hash wget 2>/dev/null; then
		echo "wget not installed, installing now."
		yum install -y wget
	fi
}

# --------------------------------------------------------- # 
# doesAnsibleVersionExist()
# Checks if ansible exists, then checks if the desired 
# version is installed.
# Returns: 0 if success, 1 if failure
# --------------------------------------------------------- # 
function doesAnsibleVersionExist() {
	if hash ansible 2>/dev/null; then
		local DESIRED_VERSION="$(ansible --version)"
		if [[ "$DESIRED_VERSION" == "ansible $1" ]]; then
			return 0
		fi
	fi

	return 1
}

# --------------------------------------------------------- # 
# installAnsible()
# Installs desired version of ansible if it's not installed
# --------------------------------------------------------- # 
function installAnsible() {
	echo "Starting..."
	ANSIBLE_INSTALL_DIR=/tmp
	local DESIRED_VERSION=$1

	doesAnsibleVersionExist $DESIRED_VERSION
	local return_val="$?"

	if [ $return_val -eq 0 ]; then
		echo "Correct version of ansible is installed, proceed."
		return 0
	fi

	echo "Downloading ansible v$DESIRED_VERSION"
	installWget
	wget -O $ANSIBLE_INSTALL_DIR/ansible-$DESIRED_VERSION.tgz https://github.com/ansible/ansible/archive/v$DESIRED_VERSION.tar.gz

	echo "Extract ansible to $ANSIBLE_INSTALL_DIR"
	tar xzf $ANSIBLE_INSTALL_DIR/ansible-$DESIRED_VERSION.tgz -C $ANSIBLE_INSTALL_DIR

	echo "Adding EPEL repo"
	rpm -Uvh http://download.fedoraproject.org/pub/epel/6/i386/epel-release-6-8.noarch.rpm

	echo "Installing ansible dependencies for rpm creation"
	yum install -y rpm-build make python2-devel

	echo "Build ansible rpm"
	cd $ANSIBLE_INSTALL_DIR/ansible-$DESIRED_VERSION
	make rpm

	echo "Install ansible $DESIRED_VERSION"
	yum --nogpgcheck localinstall -y rpm-build/ansible-*.noarch.rpm

}
