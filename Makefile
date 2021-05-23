EXPORT_DIR = export
ZIP = zip
EXPORT_NAME = endlemar-pjv-semestral-work-chess
EXPORT_VERSION = $(shell git describe --long --always --dirty="-dirty")

# ASCII color sequences
# credits: https://stackoverflow.com/questions/5947742/how-to-change-the-output-color-of-echo-in-linux
# see also: https://unix.stackexchange.com/questions/269077/tput-setaf-color-table-how-to-determine-color-codes
# to get all 256 colors:
#   for c in {0..255}; do tput setaf $c; tput setaf $c | cat -v; echo =$c; done
red = $(shell tput setaf 1)
green = $(shell tput setaf 2)
yellow = $(shell tput setaf 3)
cyan = $(shell tput setaf 6)
gray = $(shell tput setaf 8)
bl = $(shell tput bold)
rs = $(shell tput sgr0)

# BRUTE export
# - exports the whole project sources as ZIP
# - in order to include only the project sources (i.e. the versioned files),
#   we want to respect the .gitignore
# - let the git to print the filenames of version all files using
#     git ls-tree --full-tree -r --name-only HEAD (only committed)
#     git ls-files (all)
#     (see https://stackoverflow.com/questions/8533202/list-files-in-local-git-repo)
#   and then supply these names as input files for zip
# - note: `-z` option makes git ls-tree use \0 byte as separator
#         which works then with xargs -0 option and together correctly handles spaces in filenames
$(EXPORT_DIR):
	@echo "$(bl)$(yellow)Exporting the whole project as ZIP for BRUTE ...$(rs)"
	mkdir -p $(EXPORT_DIR)
	git ls-tree -z --full-tree -r --name-only HEAD | xargs -0 zip $(EXPORT_DIR)/$(EXPORT_NAME)-$(EXPORT_VERSION).zip
	@echo "$(bl)$(green)project successfully exported to $(cyan)$(EXPORT_DIR)/$(EXPORT_NAME)-$(EXPORT_VERSION).zip$(rs)"

# Documentation
doc:
	@echo "$(bl)$(yellow)Generating docs ...$(rs)"
	@echo "$(bl)$(yellow)... cleaning up previous output ...$(rs)"
	rm -rf docs/javadoc/dist
	@echo "$(bl)$(yellow)... running maven-javadoc-fix.sh ...$(rs)"
	@./docs/javadoc/maven-javadoc-fix.sh
	@echo "$(bl)$(yellow)... adding robots.txt"
	cp docs/javadoc/robots.txt docs/javadoc/dist/robots.txt

doc-deploy: doc
	@echo "$(bl)$(yellow)Deploying docs ...$(rs)"
	(cd docs/javadoc && netlify deploy --dir dist --prod)

clean:
	rm -rf $(EXPORT_DIR) docs/javadoc/dist

# what is .PHONY? see https://www.gnu.org/software/make/manual/html_node/Phony-Targets.html
.PHONY: $(EXPORT_DIR) doc doc-deploy clean
