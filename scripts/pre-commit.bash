#!/usr/bin/env bash

echo "Running pre-commit hook"
./scripts/run-tests.bash

# $? stores exit value of the last command
# shellcheck disable=SC2181
if [ $? -ne 0 ]; then
  echo "Tests must pass before commit!"
  exit 1
fi