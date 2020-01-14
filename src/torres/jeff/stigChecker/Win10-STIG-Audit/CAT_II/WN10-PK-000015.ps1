## Windows 10 STIG Compliance Audit
## Created by Alex Blackburn

$GroupID = "V-63587"
$Hostname = (Get-WmiObject Win32_ComputerSystem).Name
$Title = "Windows 10 Security Technical Implementation Guide"


if ($Hostname -eq $null){

$Hostname = $env:computername}

$Configuration = ""
$Regkey = (Get-ChildItem -Path Cert:Localmachine\disallowed | Where {$_.Issuer -Like "*DoD Interoperability*" -and $_.Subject -Like "*DoD*"}).Thumbprint
if ($regkey -contains '22BBE981F0694D246CC1472ED2B021DC8540A22F' -and 'AC06108CA348CC03B53795C64BF84403C1DBD341'){
    $regkeydate = (Get-ChildItem -Path Cert:Localmachine\disallowed | Where {$_.Issuer -Like "*DoD Interoperability*" -and $_.Subject -Like "*DoD*"}).NotAfter
     if($regkeydate.year -lt $date.year) {
        $Configuration = 'Ongoing'}
        else{
        $Configuration = 'Completed'}}
    else{
    $Configuration = 'Ongoing'}




$Audit = New-Object -TypeName System.Object
$Audit | Add-Member -MemberType NoteProperty -Name GroupID -Value $GroupID
$Audit | Add-Member -MemberType NoteProperty -Name Hostname -Value $Hostname
$Audit | Add-Member -MemberType NoteProperty -Name Configuration -Value $Configuration
$Audit | Add-Member -MemberType NoteProperty -Name Title -Value $Title
$Audit