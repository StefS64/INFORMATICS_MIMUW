#!/bin/bash

# Linijkę z kompilacja można usunąć.
# python3 generator.py N TEST_GROUP OUTPUT_FILE SEED ID
make tester # cicho tam
make tester_abi


echo -e "\n\n"
echo ===================================
echo ===== TESTUJĘ DLA ABI TESTU ======
echo ===================================
./tester_abi
if [ $? != 0 ]; then
    echo -e "\e[31mProgram FAILED on test ABI :(\e[0m"
    exit
fi
echo -e "Test \e[95mABI\e[0m \e[32mZDAŁEM\e[0m"

# Config
# Ziarno jest zależne od wartości id!
id=0
SMOL=2000
DUZE=3000
OVERFLOW=1000


echo -e "\n\n"
echo ====================================
echo ===== TESTUJĘ DLA SMOL TESTÓW ======
echo ====================================
n=1
for((i=0; i<SMOL; i++)); do
    python3 generator.py $n S test_input.txt $i $id
    ./tester < test_input.txt > test_output.txt
    if [ $? -eq 1 ]; then
        echo -e "\e[31mProgram FAILED on test $i :(\e[0m"
        echo "Test data save in files"
        exit
    fi
    echo -ne "\rTest nr $i:  n=$n \e[36mSMOL\e[0m seed=$i \e[32mPASSED\e[0m" "x=`sed '2q;d' test_input.txt` y=`sed '3q;d' test_input.txt`        "
    ((id++))
done


echo -e "\n\n"
echo ==============================================
echo "===== TESTUJĘ DLA BOLSZOJ (FULL) TESTÓW ======"
echo ==============================================
n=10000
for((i=0; i<DUZE;i++)); do
    python3 generator.py $n F test_input.txt $id $id
    ./tester < test_input.txt > test_output.txt # to jest mdiv_example po prostu
    if [ $? -eq 1 ]; then # ay caramba
        echo -e "\e[31mProgram FAILED on test $i :(\e[0m"
        echo "Test data save in files"
        exit
    fi
    echo -ne "\rTest nr $i:  n=$n \e[33mBOLSZOJ\e[0m seed=$id \e[32mPASSED\e[0m"
    ((id++))
done


# Define the trap handler function to handle SIGFPE
handle_sigfpe() {
    # echo "Received SIGFPE"
    # echo -ne ""
    return
}
# Set up the trap to catch SIGFPE and call the handler function
trap 'handle_sigfpe' SIGFPE


echo -e "\n\n"
echo ========================================
echo ===== TESTUJĘ DLA OVERFLOW TESTÓW ======
echo ========================================
n=1000
for((i=1; i<OVERFLOW;i++)); do 
    python3 generator.py $i O test_input.txt $id $id # to ma sens tylko dla długości różnych
    error_message=$(./tester < test_input.txt 2>&1 >/dev/null)  # bash wypisywał mi tutaj info o błędzie i niemogłem obejść...
    errn=$?
    if [ $errn -ne 136 ]; then # ay caramba, SIGFPE = 128 + 8(kod fpe)
        echo -e "\e[31mProgram FAILED on test $i :(\e[0m"
        echo "Test data saved."
        exit
    fi
    echo -ne "\rTest nr $i:  n=$i \e[94mOVERFLOW\e[0m seed=$id \e[32mPASSED\e[0m  errno $errn (=136)"
    ((id++))
done

echo ""
echo -e "\e[42;97m+------------------------------------+\e[0m"
echo -e "\e[42;97m|----]  🙂🙂🙂🙂🙂🙂🙂🙂🙂🙂    [----|\e[0m"
echo -e "\e[42;1;97m|----] ALL of $id tests PASSED! [----|\e[0m"
echo -e "\e[42;97m|----]  🙃🙃🙃🙃🙃🙃🙃🙃🙃🙃    [----|\e[0m"
echo -e "\e[42;97m+------------------------------------+\e[0m"

