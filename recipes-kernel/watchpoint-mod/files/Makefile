# Makefile
# Copyright 2024 Roman Tsykaliak
#   This program is free software: you can redistribute it and/or
# modify it under the terms of the GNU General Public License as
# published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#   This program is distributed in the hope that it will be useful, but
# WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#   You should have received a copy of the GNU General Public License
# along with this program.  If not, see
# <https://www.gnu.org/licenses/>.
obj-m := watchpoint.o
SRC := $(shell pwd)
.PHONY:all
all:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC)
.PHONY:modules_install
modules_install:
	$(MAKE) -C $(KERNEL_SRC) M=$(SRC) modules_install
.PHONY:clean
clean:
	rm -f *.o *~ core .depend .*.cmd *.ko *.mod.c
	rm -f Module.markers Module.symvers modules.order
	rm -rf .tmp_versions Modules.symvers
