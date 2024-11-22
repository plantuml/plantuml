#!/bin/bash

# Make script executable
chmod +x test_nassi.sh

# Define paths
PLANTUML_JAR="build/libs/plantuml-1.2024.8beta7.jar"
TEST_DIR="test/nassi"
OUTPUT_DIR="output/nassi"

# Create directories if they don't exist
mkdir -p $OUTPUT_DIR
mkdir -p $TEST_DIR

# Build the project first
./gradlew clean build

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Build failed!"
    exit 1
fi

# Function to process a single file
process_file() {
    local file=$1
    local basename=$(basename "$file" .puml)
    echo "Processing $basename..."
    
    java -jar $PLANTUML_JAR "$file" -o "$OUTPUT_DIR"
    
    if [ $? -eq 0 ]; then
        echo "✓ Successfully generated $basename"
    else
        echo "✗ Error generating $basename"
        return 1
    fi
}

# Create test files if they don't exist
cat > "$TEST_DIR/01_simple.puml" << 'EOL'
@startnassi
title "Simple Sequential Flow"
block "Initialize x = 0"
block "Add 5 to x"
block "Multiply x by 2"
block "Print result"
@endnassi
EOL

cat > "$TEST_DIR/02_if_only.puml" << 'EOL'
@startnassi
title "Basic If Statement"
block "Get user input"
if "input > 10" then
    block "Value is large"
else
    block "Value is small"
endif
block "Done"
@endnassi
EOL

cat > "$TEST_DIR/03_while_loop.puml" << 'EOL'
@startnassi
title "While Loop Example"
block "counter = 0"
while "counter < 5" do
    block "Print counter"
    block "Increment counter"
endwhile
block "Loop finished"
@endnassi
EOL

cat > "$TEST_DIR/04_nested_control.puml" << 'EOL'
@startnassi
title "Nested Control Structures"
input "Get number n"
while "n > 0" do
    if "n % 2 == 0" then
        output "Even number found"
        if "n % 4 == 0" then
            block "Divisible by 4"
        else
            block "Not divisible by 4"
        endif
    else
        output "Odd number found"
    endif
    block "Decrement n"
endwhile
block "Processing complete"
@endnassi
EOL

cat > "$TEST_DIR/05_complex.puml" << 'EOL'
@startnassi
title "Complex Algorithm Example"
input "Get array size n"
block "Initialize array"
while "i < n" do
    input "Get element"
    if "element < 0" then
        block "Skip negative"
        break "Continue to next"
    else
        if "element > max" then
            block "Update max"
        else
            if "element < min" then
                block "Update min"
            endif
        endif
        block "Add to sum"
    endif
    block "i = i + 1"
endwhile
if "sum > 1000" then
    while "sum > 1000" do
        block "Reduce sum"
    endwhile
    output "Sum reduced"
else
    output "Sum is fine"
endif
call "ProcessResults(sum, min, max)"
connector "A"
block "End processing"
@endnassi
EOL

# Create more test files
cat > "$TEST_DIR/06_input_output.puml" << 'EOL'
@startnassi
title "Data Processing Flow"
input "Enter data source path"
input "Enter output format"
if "File exists" then
    input "Read configuration"
    while "Has more data" do
        input "Read record"
        if "Valid format" then
            output "Processing record"
            block "Transform data"
            output "Record processed"
        else
            output "Invalid record found"
            break "Skip to next"
        endif
    endwhile
    output "Processing complete"
else
    output "File not found"
endif
@endnassi
EOL

cat > "$TEST_DIR/07_function_calls.puml" << 'EOL'
@startnassi
title "Function Call Example"
block "Initialize System"
call "LoadConfiguration()"
if "ConfigValid()" then
    call "InitializeModules()"
    while "HasPendingTasks()" do
        call "ProcessNextTask()"
        if "TaskFailed()" then
            call "LogError()"
            call "NotifyAdmin()"
            break "Stop processing"
        endif
    endwhile
    call "Cleanup()"
else
    call "ReportConfigError()"
endif
block "End"
@endnassi
EOL

cat > "$TEST_DIR/08_deep_nesting.puml" << 'EOL'
@startnassi
title "Deeply Nested Control Structures"
while "Main loop" do
    if "Condition 1" then
        while "Sub loop 1" do
            if "Condition 2" then
                while "Sub loop 2" do
                    block "Deep action 1"
                    if "Condition 3" then
                        block "Deep action 2"
                    else
                        break "Exit deep loop"
                    endif
                endwhile
            else
                block "Alternative action"
            endif
        endwhile
    else
        block "Main alternative"
    endif
endwhile
@endnassi
EOL

cat > "$TEST_DIR/09_error_handling.puml" << 'EOL'
@startnassi
title "Error Handling Flow"
block "Start Transaction"
if "Database Connected" then
    while "Process Records" do
        if "Record Valid" then
            if "Has Required Fields" then
                block "Process Record"
                if "Processing Error" then
                    call "LogError()"
                    call "RollbackTransaction()"
                    break "Stop Processing"
                else
                    call "CommitRecord()"
                endif
            else
                output "Missing Fields"
                call "SkipRecord()"
            endif
        else
            output "Invalid Record"
            if "Can Recover" then
                call "AttemptRecovery()"
            else
                call "LogFailure()"
            endif
        endif
    endwhile
else
    output "Connection Failed"
    call "NotifyAdmin()"
endif
@endnassi
EOL

cat > "$TEST_DIR/10_complex_algorithm.puml" << 'EOL'
@startnassi
title "Complex Algorithm Implementation"
input "Get dataset size N"
input "Get threshold T"
block "Initialize data structures"
while "Processing batches" do
    if "Batch size > 0" then
        while "Items in batch" do
            if "Item needs processing" then
                if "Priority high" then
                    call "FastProcess()"
                    output "Fast track result"
                else
                    if "Can be delayed" then
                        call "QueueForLater()"
                    else
                        call "NormalProcess()"
                        if "Process failed" then
                            if "Can retry" then
                                block "Increment retry count"
                                continue
                            else
                                output "Max retries exceeded"
                                break "Skip to next batch"
                            endif
                        endif
                    endif
                endif
            else
                block "Mark as skipped"
            endif
        endwhile
        if "Batch complete" then
            call "CommitBatch()"
            output "Batch statistics"
        else
            call "RollbackBatch()"
        endif
    else
        break "No more batches"
    endif
endwhile
connector "A"
block "Finalize processing"
@endnassi
EOL

cat > "$TEST_DIR/11_database_migration.puml" << 'EOL'
@startnassi
title "Database Migration Process"
block "Start Migration"
input "Get Database Credentials"
if "Connect to Source DB" then
    if "Connect to Target DB" then
        while "Processing Tables" do
            block "Get Table Schema"
            if "Schema Compatible" then
                while "Batch Processing" do
                    block "Read 1000 Records"
                    if "Transform Required" then
                        call "TransformData()"
                        if "Validation Failed" then
                            output "Log Validation Error"
                            call "NotifyAdmin()"
                            break "Skip Batch"
                        endif
                    endif
                    if "Insert Batch" then
                        call "CommitTransaction()"
                    else
                        call "RollbackTransaction()"
                        output "Batch Failed"
                        if "Retry Count < 3" then
                            block "Increment Retry"
                            continue
                        else
                            break "Max Retries Exceeded"
                        endif
                    endif
                endwhile
            else
                output "Schema Mismatch"
                call "LogSchemaError()"
            endif
        endwhile
        call "ValidateMigration()"
    else
        output "Target DB Connection Failed"
    endif
else
    output "Source DB Connection Failed"
endif
connector "END"
@endnassi
EOL

cat > "$TEST_DIR/12_payment_processing.puml" << 'EOL'
@startnassi
title "Payment Processing System"
input "Receive Payment Request"
call "ValidateRequest()"
if "Request Valid" then
    while "Processing Payment" do
        if "Payment Method" then
            if "Credit Card" then
                call "ValidateCard()"
                if "Card Valid" then
                    call "ProcessCCPayment()"
                else
                    output "Invalid Card"
                    break "Payment Failed"
                endif
            else
                if "Bank Transfer" then
                    call "ValidateAccount()"
                    if "Account Valid" then
                        call "ProcessBankTransfer()"
                    else
                        output "Invalid Account"
                        break "Payment Failed"
                    endif
                else
                    if "Digital Wallet" then
                        call "ValidateWallet()"
                        while "Processing Wallet" do
                            if "Sufficient Funds" then
                                call "ProcessWalletPayment()"
                                break "Payment Complete"
                            else
                                output "Insufficient Funds"
                                if "Can Retry" then
                                    block "Wait 30 seconds"
                                    continue
                                else
                                    break "Payment Failed"
                                endif
                            endif
                        endwhile
                    else
                        output "Unsupported Payment Method"
                        break "Payment Failed"
                    endif
                endif
            endif
        endif
    endwhile
else
    output "Invalid Request"
endif
connector "END"
@endnassi
EOL

cat > "$TEST_DIR/13_order_fulfillment.puml" << 'EOL'
@startnassi
title "E-Commerce Order Fulfillment"
input "Receive Order"
call "ValidateOrder()"
if "Order Valid" then
    block "Begin Transaction"
    while "Processing Items" do
        if "Check Stock" then
            if "In Stock" then
                if "Reserve Item" then
                    call "UpdateInventory()"
                    if "Update Failed" then
                        call "RollbackReservation()"
                        output "Inventory Error"
                        break "Processing Failed"
                    endif
                else
                    output "Reservation Failed"
                    break "Cannot Reserve"
                endif
            else
                if "Check Backorder" then
                    if "Can Backorder" then
                        call "CreateBackorder()"
                        output "Backorder Created"
                    else
                        if "Check Alternatives" then
                            while "Finding Alternative" do
                                call "SearchAlternative()"
                                if "Alternative Found" then
                                    if "Customer Accepts" then
                                        call "UpdateOrder()"
                                        break "Alternative Accepted"
                                    endif
                                else
                                    break "No Alternative"
                                endif
                            endwhile
                        else
                            output "Item Unavailable"
                            break "Cannot Fulfill"
                        endif
                    endif
                endif
            endif
        endif
    endwhile
    if "All Items Processed" then
        call "FinalizeOrder()"
        output "Order Confirmed"
    else
        call "RollbackTransaction()"
        output "Order Failed"
    endif
else
    output "Invalid Order"
endif
connector "END"
@endnassi
EOL

cat > "$TEST_DIR/14_data_pipeline.puml" << 'EOL'
@startnassi
title "Big Data Processing Pipeline"
input "Configure Pipeline"
call "InitializeCluster()"
if "Cluster Ready" then
    while "Processing Batches" do
        input "Read Data Chunk"
        if "Validate Schema" then
            while "Transform Phase" do
                if "Apply Transformations" then
                    if "Data Quality Check" then
                        call "EnrichData()"
                        if "Enrichment Success" then
                            while "Parallel Processing" do
                                if "Process Partition" then
                                    call "ApplyBusinessRules()"
                                    if "Rules Passed" then
                                        call "AggregateResults()"
                                    else
                                        output "Rule Violation"
                                        if "Can Correct" then
                                            call "AutoCorrect()"
                                            continue
                                        else
                                            break "Invalid Data"
                                        endif
                                    endif
                                else
                                    output "Processing Error"
                                    break "Partition Failed"
                                endif
                            endwhile
                        else
                            output "Enrichment Failed"
                            break "Cannot Enrich"
                        endif
                    else
                        output "Quality Check Failed"
                        break "Poor Quality"
                    endif
                else
                    output "Transform Error"
                    break "Cannot Transform"
                endif
            endwhile
            if "All Transforms Complete" then
                while "Output Phase" do
                    if "Prepare Output" then
                        call "FormatOutput()"
                        if "Write Output" then
                            call "CommitResults()"
                            output "Batch Complete"
                        else
                            call "RollbackBatch()"
                            break "Write Failed"
                        endif
                    else
                        output "Format Error"
                        break "Cannot Format"
                    endif
                endwhile
            endif
        else
            output "Schema Validation Failed"
        endif
    endwhile
else
    output "Cluster Initialization Failed"
endif
connector "END"
@endnassi
EOL

cat > "$TEST_DIR/15_ai_training.puml" << 'EOL'
@startnassi
title "AI Model Training Pipeline"
input "Load Configuration"
call "InitializeEnvironment()"
if "Environment Ready" then
    input "Load Training Data"
    if "Validate Dataset" then
        while "Data Preprocessing" do
            if "Clean Data" then
                call "RemoveOutliers()"
                if "Feature Engineering" then
                    while "Feature Selection" do
                        if "Analyze Feature" then
                            if "Feature Important" then
                                call "TransformFeature()"
                                if "Transform Success" then
                                    block "Add to Feature Set"
                                else
                                    output "Transform Failed"
                                    if "Can Skip" then
                                        continue
                                    else
                                        break "Critical Feature Failed"
                                    endif
                                endif
                            else
                                block "Skip Feature"
                            endif
                        else
                            output "Analysis Failed"
                            break "Cannot Analyze"
                        endif
                    endwhile
                    if "Feature Set Complete" then
                        while "Model Training" do
                            if "Initialize Model" then
                                while "Training Epochs" do
                                    call "TrainBatch()"
                                    if "Evaluate Performance" then
                                        if "Performance Improved" then
                                            call "SaveCheckpoint()"
                                            if "Convergence Check" then
                                                break "Training Complete"
                                            endif
                                        else
                                            if "Early Stopping" then
                                                break "No Improvement"
                                            else
                                                if "Adjust Parameters" then
                                                    call "UpdateHyperparameters()"
                                                    continue
                                                else
                                                    break "Cannot Optimize"
                                                endif
                                            endif
                                        endif
                                    else
                                        output "Evaluation Failed"
                                        break "Cannot Evaluate"
                                    endif
                                endwhile
                            else
                                output "Model Initialization Failed"
                            endif
                        endwhile
                    endif
                else
                    output "Feature Engineering Failed"
                endif
            else
                output "Data Cleaning Failed"
            endif
        endwhile
    else
        output "Invalid Dataset"
    endif
else
    output "Environment Setup Failed"
endif
connector "END"
@endnassi
EOL

# Process all test files
echo "Starting test suite..."
echo "===================="

failed=0
total=0

for file in "$TEST_DIR"/*.puml; do
    total=$((total + 1))
    if ! process_file "$file"; then
        failed=$((failed + 1))
    fi
    echo "------------------"
done

echo "===================="
echo "Test suite complete"
echo "Processed: $total files"
echo "Successful: $((total - failed))"
echo "Failed: $failed"

if [ $failed -eq 0 ]; then
    echo "All diagrams generated successfully!"
    echo "Output saved to: $OUTPUT_DIR/"
    exit 0
else
    echo "Some diagrams failed to generate"
    exit 1
fi 